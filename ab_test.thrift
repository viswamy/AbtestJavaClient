namespace java com.vswamy.ab_testing

exception NullOrEmptyException 
{
   1: string message
}

exception ExperimentNotFoundException 
{
   1: string message
}

exception ExperimentAlreadyExistsException 
{
   1: string message
}

exception InvalidPasscodeException 
{
   1: string message
}

struct Experiment
{
	1: string experimentName,
	2: map<string, i32> stateWeights,
	3: string authorName,
	4: string authorEmailAddress,
	5: string passcode
}

service ExperimentService
{
    bool ping(),
    
    string getExperimentState(1: string experimentName) throws (
    		1: ExperimentNotFoundException experimentNotFoundException, 
    		2: NullOrEmptyException nullOrEmptyException
    		),
    
    // Does not throw exception if certain experiments are not found instead, returns as much as possible.
    map<string,string> getExperimentsState(1: list<string> experimentsName) throws (
    		1: NullOrEmptyException nullOrEmptyException
    		), 
    
    //passcode will always be either NULL or empty string
    Experiment getExperiment(1: string experimentName) throws (
    		1: ExperimentNotFoundException experimentNotFoundException, 
    		2: NullOrEmptyException nullOrEmptyException
    		), 
    
    bool createExperiment(1: Experiment experiment) throws (
    		1: ExperimentAlreadyExistsException experimentAlreadyExistsException, 
    		2: NullOrEmptyException nullOrEmptyException
    		),
    
    bool editExperiment(1: Experiment experiment) throws (
    		1: InvalidPasscodeException invalidPasscodeException, 
    		2: NullOrEmptyException nullOrEmptyException,
    		3: ExperimentNotFoundException experimentNotFoundException
    		)

}







