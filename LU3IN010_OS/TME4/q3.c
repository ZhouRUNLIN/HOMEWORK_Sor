#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

typedef struct cpu_data_{
	char name[20];
	unsigned int l1_user_data;
	unsigned int l2_unknown_data;
	unsigned int l3_system_data;
	unsigned int l4_unknown_data;
	unsigned int l5_io_data;
	unsigned int l6_irq_data;
	unsigned int l7_sirq_data;
}cpu_data;

double cal_cpu (cpu_data *c1, cpu_data *c2)
{
	double deltaT, deltaD;
	double id, sd;
	double pourcent_cpu ;
	
	deltaT =(double)((c2->l1_user_data + c2->l2_unknown_data + c2->l3_system_data +c2->l4_unknown_data+c2->l5_io_data+c2->l6_irq_data+c2->l7_sirq_data)-(c1->l1_user_data + c1->l2_unknown_data + c1->l3_system_data +c1->l4_unknown_data+c1->l5_io_data+c1->l6_irq_data+c1->l7_sirq_data));
	
	deltaD = (double) (c2->l4_unknown_data - c1->l4_unknown_data);
	if(deltaT != 0)
		pourcent_cpu =100.0 - deltaD/deltaT*100.00;
	else 
		pourcent_cpu = 0;
	return pourcent_cpu;
}

void read_file_stat (cpu_data *cpust)
{
	FILE *fd;
	int n;
	char buff[256];
	cpu_data *cpu_occupy;
	cpu_occupy=cpust;
	
	fd = fopen ("/proc/stat", "r");
	if(fd == NULL)
		{
			perror("fopen:");
			exit (0);
		}
	fgets (buff, sizeof(buff), fd);
	
	sscanf (buff, "%s %u %u %u %u %u %u %u", cpu_occupy->name, &cpu_occupy->l1_user_data, &cpu_occupy->l2_unknown_data,&cpu_occupy->l3_system_data, &cpu_occupy->l4_unknown_data ,&cpu_occupy->l5_io_data,&cpu_occupy->l6_irq_data,&cpu_occupy->l7_sirq_data);
	
	fclose(fd);
}

double str_cpu()
{
	cpu_data cpu_stat1;
	cpu_data cpu_stat2;
	double cpu;
	read_file_stat((cpu_data *)&cpu_stat1);
	sleep(1);
	read_file_stat((cpu_data *)&cpu_stat2);
	cpu = cal_cpu ((cpu_data *)&cpu_stat1, (cpu_data *)&cpu_stat2);
	
	return cpu;
}

int main(int argc, char *argv[]){
	char* command = argv[1];
	int p = 0; int i = 2;
	while(i<argc && p==0){
		if((p=fork()) == 0){
			execl("./", "grep", command, argv[i]);
			printf("Created proc %d, parent pid %d, user pid %d\n",getpid(),getppid(),getuid());
			i++;
		}
		else {
			printf("CPU : %f\n",str_cpu());
		}
	}
	if(p != 0) wait(NULL);
	printf("p:%d i:%d\n",p,i);
	return 0;
}