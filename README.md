## About

This is a Csv file Streaming app to process file of the below Structure, currently the program uses 5 mb as the chunk
size to read the csv file, it's not parametrized if one wish to change the chunk size it can be done

`readAsStream(<path to dir>, <chunkSize>)`

chunkSize is an Double which 

### **Ex. fileName: leader-1.csv**

<table>
  <tr>
    <td>sensor-id</td>
    <td>humidity</td>
  </tr>
  <tr>
    <td>s1</td>
    <td>10</td>
  </tr>
  <tr>
    <td>s2</td>
    <td>88</td>
  </tr>
</table>

## HOW TO BUILD PROJECT

### Build Jar:
  Step 1: 
  *run the below command* on your terminal
```
 sbt clean compile test assembly
```
Successful run of the above command should generate a jar file with name
`humidity-sensor-app.jar`

Navigate to the jar file and run the below command, it's using 200Mb of heap size please modify accordingly.
```
 java -Xmx200m -jar ./humidity-sensor-app.jar <Path to director>
```
If the Path is not provided as the command line argument Application will fail with FileNotFoundException

**Note:**
<span style="font-weight: bold; color: #E5514E;">Argument Path Should Not End with /</span>


### Build Docker Image
  Step 1:
    *Start local docker and run the following command to create and publish the docker Image in your local*
 ```
 sbt docker:publishLocal
```

### Data Generator
you can use `generate/CSVGenerator` to generate the test data
update the below parameter in the file, it will also generate a expected output file having min,avg,max,count,total to verify the 
program

```
val fileName = "Leader9.csv"
val outputFilePath = s"src/test/resources/data/$fileName"
val expectedOutput = s"src/test/resources/$fileName-expected.csv"
val fileSizeMB = 1
```