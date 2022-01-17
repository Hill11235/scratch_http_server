## CS5001 Assignment 3

I have attempted the following advanced requirements:
* Multithreading - my ServerHandler class implements the Runnable interface and so can be threaded. Threads are created in the HTTPServerHub class and so clients can connect concurrently.
* Logging - using the java.util.logging package I have implemented an HTTPLogger class which logs each request and the corresponding response header. An XML file called "HTTPLog.log" is created in the same directory as the classes and the requests and responses are logged here. The logging class has a synchronised public method which allows threads to write to the log safely. Each time the WebServer is restarted the log is overwritten and started anew.

I have included a unit testing class "RegexTester.java" in X directory. I used this to ensure that the regex testing class I wrote behaves as desired. I have included the necessary hamcrest and junit jar files as well. To use:
* If using an IDE add JUnit to the build path.
* Otherwise to compile `javac -cp <...>/junit.jar:<...>/hamcrest.jar:<path_to_src>:. *.java` where <...> is path to directory of classes to be tested.
* To run unit tests `java -cp <...>/junit.jar:<...>/hamcrest.jar:<path_to_src>:.
org.junit.runner.JUnitCore <junit_classes>`
