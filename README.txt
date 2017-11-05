There are two primary applications in this project:
    1. CensusAnalyzer --> This application reads information from a US Census file that contains data about US Shool Districts and Child Poverty in corresponding districts. This application strips off unneeded data, and also aggregates district information to give a state summary. Command line arguments:
        (1) input filename
        (2) output filename
        (3) number of records to read (optional; reads entire file if not supplied)
    
    2. CensusDataOutputReport --> This report accepts the aggregated district information file, then displays the information to the standard output.Command line arguments:
        (1) input filename
        (2) number of records to read (optional; reads entire file if not supplied)

    Since CensusAnalyzer generates as its output a file to be accepted as input to the CensusDataOutputReport application, it is important to run the two applications in the order listed above. Once a file has been created by CensusAnalyzer, it can be read into the CensusDataOutputReport application multiple times without rerunning CensusAnalyzer (CensusDataOutputReport does not delete the file it reads).

    Important Notes
    ==============================================================
    1. The application is built to accept up to 60 states. Since the project sponsor required that we only use standard arrays, we do not have a mechanism of increasing array size on an as needed basis. Hence, we picked a logical maximum of 60, which allows for all 50 states, with room for additional information for territories, or new states (as necessary).

    2. The CensusAnalyzer application presumes that the data layout of its input file is fixed and will not change. The input file provides no logical delimeter, so we used the layout as provided in the detail file.

