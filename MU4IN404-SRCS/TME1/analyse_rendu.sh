#!/bin/bash

usage="$(basename $0) [-t] <fichier zip ressources> <fichier zip rendu>"

dir_unzip=$(mktemp -d -t analyseTP-XXXXXXXXXXXX)

red=`tput setaf 1`
green=`tput setaf 2`
orange=`tput setaf 3`
blue=`tput setaf 4`
reset=`tput sgr0`

file_bilan="$dir_unzip/bilan"
existing_fail="false"

export JAVA_OPTS="-Xmx2g" # taille du tas de la jvm

fail() {
	echo "${red} $1 ${reset}"
	existing_fail="true"
	echo "${red} $1 ${reset}" >> $file_bilan
}

warning() {
	echo "${orange} $1 ${reset}"
}

fatal() {
	fail "$1"
	echo "${red} Arrêt du script ${reset}"
	rm -rf $dir_unzip
	exit 1
}

ok() {
  suffix=$1
  [ "$suffix" != "" ] && suffix="($suffix)"
	echo "${green}OK $suffix ${reset}"
}

getopts "t" shouldtest
[ "$shouldtest" = "t" ] && shouldtest="true" || shouldtest="false" 

$shouldtest && shift

[ $# -lt 2 ] && echo $usage && exit 1
fichier_ress_zip=$1
fichier_rend_zip=$2

echo -n "Option d'execution des tests : "
$shouldtest && echo "${green}oui${reset}" || warning "non"

echo -n "test de la présence de la commmande jq : "
which jq > /dev/null && ok $(which jq)  || fatal "la commande jq doit être installée"

echo -n "état de la variable d'environnement CLASSPATH : "
if $shouldtest ; then 
	[ "$CLASSPATH" != "" ] && ok $CLASSPATH  || fatal "VIDE (l'option test est activée, doit contenir a minima les jar de Junit)"
else
	[ "$CLASSPATH" != "" ] && ok $CLASSPATH  || warning "VIDE (attention ceci peut être problématique si votre code dépend d'une librairie tierce)"
fi



echo -n "test existence du fichier $fichier_ress_zip : "
[ -f "$fichier_ress_zip"  ] && ok  || fatal "$fichier_ress_zip doit être un fichier "
echo -n "test existence du fichier $fichier_ress_zip : "
[ -f "$fichier_rend_zip"  ] && ok || fatal "$fichier_rend_zip doit être un fichier "

echo -n "$fichier_ress_zip est un fichier zip : "
[ "$(file "$fichier_ress_zip" | cut -d ':' -f2 | grep Zip )" != ""  ] && ok || fatal "$fichier_ress_zip doit être un fichier zip"
echo -n "$fichier_rend_zip est fichier zip : "
[ "$(file "$fichier_rend_zip" | cut -d ':' -f2 | grep Zip )" != ""  ] && ok || fatal "$fichier_rend_zip doit être un fichier zip"

dir_ress=$dir_unzip/$(zipinfo -1 "$fichier_ress_zip" | head -1 | cut -d '/' -f1 )
dir_rend=$dir_unzip/$(zipinfo -1 "$fichier_rend_zip" | head -1 | cut -d '/' -f1)

echo "repertoire de ressource : $dir_ress"
echo "repertoire de rendu : $dir_rend"

echo -n "Dossier de ressouces différent du dossier de rendu : "
[ "$dir_ress" != "$dir_rend" ] && ok || fatal "les dossiers de ressources et de rendu sont les mêmes"

echo -n "decompression $fichier_ress_zip dans $dir_unzip : "
unzip -o "$fichier_ress_zip" -d $dir_unzip > /dev/null && ok || fatal "impossible de dezipper $fichier_ress_zip dans $dir_unzip"
echo -n "decompression $fichier_rend_zip dans $dir_unzip : "
unzip -o "$fichier_rend_zip" -d $dir_unzip > /dev/null  && ok || fatal "impossible de dezipper $fichier_rend_zip dans $dir_unzip"

dir_data="$dir_ress/data"
echo -n "Test présence d'un dossier de donnees $dir_data : "
[ -d "$dir_data" ] && ok || echo "non present"


fichier_json="$dir_ress/desc.json"
echo -n "Présence de $fichier_json : "
[ -f "$fichier_json" ]  && ok || fatal "Absent de l'archive $fichier_ress_zip"



echo -n "Commande du compilateur : "
compiler=$(cat $fichier_json | jq '.compiler' | sed 's/"//g')
optscompiler=$(cat $fichier_json | jq '.optscompiler' | sed 's/"//g')
[ "$optscompiler" = "null" ] && optscompiler=""

[ $compiler != "" ] && which $compiler >/dev/null && ok "$compiler $(! [ -z $optscompiler ] && echo "avec options $optscompiler")" || fatal "compilateur $compiler incorrect ou non installé"

echo -n "Commande d'exécution : "
command=$(cat $fichier_json | jq '.command' | sed 's/"//g')
[ $command != "" ] && which $command >/dev/null && ok "$command" || fatal "commande $command incorrect ou non installée"

echo -n "Evaluation du nombre d'exercices à tester : "
nb_exercices=$(cat $fichier_json | jq '.exercices | keys | length ') && ok "$nb_exercices exercices" || fatal "impossible de connaitre le nombre d'exercices"


export CLASSPATH=$CLASSPATH
bin_dir=$dir_unzip

exo=0

while [ $exo -lt $nb_exercices ] ;do
	name_exo=$(cat $fichier_json | jq ".exercices | keys[$exo]"| sed 's/"//g')
	echo ""
	echo "${blue}****** exercice $name_exo ******${reset}"
	
	files_required=$(cat $fichier_json | jq '.exercices."'"$name_exo"'".required | .[]'| sed 's/"//g')
	
	for f in $files_required ; do
		path_src_file="$dir_rend/$f"
		echo -n "fichier $f : " >> $file_bilan
		echo -n "Test existence $f dans $dir_rend: "
		[ -f "$path_src_file" ] && ok || fail "inexistant"
		if [ -f "$path_src_file" ] ; then
			echo -n "Tentative compilation $f : "
			[ -z $CLASSPATH ] && cp="" || cp="-classpath $CLASSPATH"
			#option incompatible avec scalac -Xdiags:verbose
			$compiler $optscompiler -d $bin_dir -sourcepath "$dir_rend:$dir_ress"  $cp $path_src_file && ok && echo "${green}compilable ${reset}" >> $file_bilan || fail "non compilable" 
			
			
			
		fi
	done
	
	tests=$(cat $fichier_json | jq '.exercices."'"$name_exo"'".tests | .[]'| sed 's/"//g')
	for test in $tests ; do
		f="$(echo $test | sed 's/\./\//g').$command"
		echo -n "Test non existence test $f dans rendu $dir_rend : "
		! [ -f "$dir_rend/$f" ] && ok || warning "fichier de test présent dans le rendu ($f)"
		if [ -f "$dir_rend/$f" ] ; then
			warning "Suppression de $f dans le rendu"
			rm "$dir_rend/$f"
		fi
		
		if ! $shouldtest ; then
			continue
		fi
		path_src_file="$dir_ress/$f"
		echo -n "test $test : " >> $file_bilan
		echo -n "Test existence $f dans $dir_ress : "
		[ -f "$path_src_file" ] && ok || fail "fichier non trouve ($f)"
		if [ -f "$path_src_file" ] ; then
			echo -n "Tentative compilation fichier de test $f : "
			if ! $compiler $optscompiler -d $bin_dir -sourcepath "$dir_rend:$dir_ress"  -classpath "$bin_dir:$CLASSPATH" $path_src_file ; then
				fail "fichier non compilable ($f)"
			else
				ok
				echo -n "Execution du test $test : "
				if ! $command -cp "$bin_dir:$CLASSPATH" -Ddata.dir="$dir_data" org.junit.runner.JUnitCore $test > /tmp/tmp 2> /tmp/tmp2 ; then 
					fail "echec"
					cat /tmp/tmp
				else 
					ok
					echo "${green}OK ${reset}">> $file_bilan
				fi
			fi
		fi

	done
	
	exo=$((exo+1))
done

echo ""
echo "${blue}************************* Resume ****************************${reset}"
cat $file_bilan
echo -n "Bilan du rendu : "

$existing_fail && fail " il existe des erreurs"
! $existing_fail && $shouldtest && ok "Tout est bon"
! $existing_fail && ! $shouldtest && warning "Ok mais pas d'execution des tests"

rm -rf $dir_unzip
