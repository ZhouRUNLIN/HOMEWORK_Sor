cd ~/Downloads/DAAR_TME_rep-main-2/projet-gutenberg/mySearchEngine &&
source ../gutenbergVEnv/bin/activate &&
python3 manage.py refreshOnSaleList >> ~/mySearchEngineLog &&
deactivate
