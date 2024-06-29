#ifndef SRC_JOB_H_
#define SRC_JOB_H_


class Job {
public:
	virtual void run () = 0;
	virtual ~Job() {};
};



#endif /* SRC_JOB_H_ */
