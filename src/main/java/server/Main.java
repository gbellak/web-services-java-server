package server;

import api.HTTPModule;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Date;

import static server.HTTPServer.PORT;
import static server.HTTPServer.verbose;

public class Main {

    public static void main(String[] args) {

        //httpMethods.addAll(Arrays.asList(new HTTPGet(), new HTTPHead(), new HTTPPost()));
        //HTTPServer.getFunctions().add(new Calculator());

        HTTPModule files = new FilesModule();
        HTTPModule calculator = new Calculator2();
        HTTPServer.getFunctions().put("files", files);
        HTTPServer.getFunctions().put("calculator", calculator);

        //URLClassLoader ucl = createClassLoader(args[0]);

//        ServiceLoader<HTTPModule> loader =
//                ServiceLoader.load(HTTPModule.class, ucl);
//
//        for (HTTPModule modules : loader) {
//        }




        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started.\nListening for connections on port: "
                    + PORT + "...");

            while (true){
                HTTPServer server = new HTTPServer(serverSocket.accept());

                if(verbose) System.out.println("Connection established. " + new Date());

                Thread thread = new Thread(server);
                thread.start();
            }

        } catch (IOException e) {
            System.err.println("Server connection error" + e.getMessage());
        }
    }

    private static URLClassLoader createClassLoader(String fileLocation){
        File loc = new File(fileLocation);// hur sätts filelocation via args, ./lib?

        File[] flist = loc.listFiles(new FileFilter() {
            public boolean accept(File file) {return file.getPath().toLowerCase().endsWith(".jar");}
        });

        URL[] urls = new URL[flist.length];
        for (int i = 0; i < flist.length; i++) {
            try {
                urls[i] = flist[i].toURI().toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return new URLClassLoader(urls);
    }
}
