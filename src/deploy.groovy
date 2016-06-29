/**
 * � Copyright IBM Corporation 2014.  
 * This is licensed under the following license.
 * The Eclipse Public 1.0 License (http://www.eclipse.org/legal/epl-v10.html)
 * U.S. Government Users Restricted Rights:  Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp. 
 */

import java.io.*;
import java.util.*;

def isEmpty(value) {
	return value == null || value.equals("")
}

//////////////////////MAIN////////////////////////
final def isWindows = (System.getProperty('os.name') =~ /(?i)windows/).find()
final def workDir = new File('.').absolutePath
final def compName = new File(".").getCanonicalFile().name //this gets resolved to component name
final def props = new Properties()
final def inputPropsFile = new File(args[0])
final def inputPropsStream = null
try {
    inputPropsStream = new FileInputStream(inputPropsFile)
    props.load(inputPropsStream)
}
catch (IOException e) {
    throw new RuntimeException(e)
}

def path = props['installRoot'] + "/node-red"
println "Install root"
println path

def flow = props['flowName'].replaceAll("\\s","")
println "Flow"
println flow

def fullFlowPath = workDir + "/" + flow
println "Full Path of Flow"
println fullFlowPath

def cmd = "cp"

def commandArgs = [cmd, "-f", fullFlowPath, path];

println commandArgs.join(' ');
def procBuilder = new ProcessBuilder(commandArgs);

if (isWindows) {
	def envMap = procBuilder.environment();
	envMap.put("PROFILE_CONFIG_ACTION","true");
}

def statusProc = procBuilder.start();
def outPrint = new PrintStream(System.out, true);
statusProc.waitForProcessOutput(outPrint, outPrint);
System.exit(statusProc.exitValue());
