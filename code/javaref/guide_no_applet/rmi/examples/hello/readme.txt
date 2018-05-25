The docs/guide/rmi/examples/hello directory in the JDK 1.1 Beta release
contains the source code for the "Hello World!" example from the RMI
Tutorial. After downloading the JDK 1.1 Beta release, if you do not
want to follow the instructions in the tutorial to build and run the
example from scratch, you can execute a script which builds and runs
the example and provides running commentary:

1.) Change to the /docs/guide/rmi/examples/hello directory
    in the JDK 1.1 Beta installation directory.

2.) On Solaris execute the run script.
    The script assumes that the registry is not currently running.

3.) On Windows systems,  execute <run.bat. Upon completion, you will
    need to explicitly destroy the windows created for the registry
    and server processes.
