namespace java com.vswamy.ab_testing

struct ExperimentState
{
	1: string name,
	2: i32 weightage,
	3: i32 cumulativeWeightage
}

struct ExperimentStateResponse 
{
   1: ExperimentState state,
   2: bool debugInfo = 0,
   3: list<ExperimentState> allStates,
   4: optional string comment,
   5: string experimentName
}

struct ExperimentStateRequest 
{
    1:string experimentName, 
    2:bool debugInfo = 0,
}

exception ExperimentNotFoundException 
{
   1: string info
}

service ExperimentService
{
    bool ping(),
    ExperimentStateResponse getExperimentState(1: ExperimentStateRequest request)
} 