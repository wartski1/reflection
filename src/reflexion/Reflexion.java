/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reflexion;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;

/**
 *
 * @author Pepe
 */
public class Reflexion {

    Package pack;
    Class clas;
    Method meto[];
    Field fiel[];
    
    public Reflexion() {       
    }
    
    public boolean cargarPaquete(String nombrePaquete){
        try {       
            pack = Package.getPackage(nombrePaquete);    
            pack.getName();            
        } catch (Exception e) {
            System.out.println("El paquete no existe");
        }         
        return true;
    }
    
    public ArrayList<String> listaClases(String nombrePaquete){
        ArrayList<String> lista = new ArrayList<>();
        ArrayList<Class<?>> classes = find(nombrePaquete);
        if (classes == null) {
            return lista;
        }
        for (Class<?> classe : classes) {
            lista.add(classe.getName());
        }       
        
        return lista;
    }
    
    public ArrayList<Class<?>> find(String scannedPackage) {
        String scannedPath = scannedPackage.replace('.', '/');
        URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedPath);
        if (scannedUrl == null) {
            System.out.println("No se pueden obtener recursos de la ruta '"+scannedPackage+"'. ¿Estás seguro de que existe el paquete '"+scannedPackage+"'?");
            return null;
        }
        File scannedDir = new File(scannedUrl.getFile());
        ArrayList<Class<?>> classes = new ArrayList<>();
        for (File file : scannedDir.listFiles()) {
            classes.addAll(find(file, scannedPackage));
        }
        return classes;
    }

    private ArrayList<Class<?>> find(File file, String scannedPackage) {
        ArrayList<Class<?>> classes = new ArrayList<>();
        String resource = scannedPackage + '.' + file.getName();
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                classes.addAll(find(child, resource));
            }
        } else if (resource.endsWith(".class")) {
            int endIndex = resource.length() - ".class".length();
            String className = resource.substring(0, endIndex);
            try {
                classes.add(Class.forName(className));
            } catch (ClassNotFoundException ignore) {
            }
        }
        return classes;
    }

    public ArrayList<String> listaMetodos(String nombreClase) {
        ArrayList<String> listaMetodos = new ArrayList<>();
        try {
            clas = Class.forName(nombreClase);
            meto = clas.getMethods();
            
            for (Method met : meto) {
                listaMetodos.add(met.getName());
            }
            
        } catch (ClassNotFoundException c) {            
        }
        return listaMetodos;
    }
    
    public ArrayList<String> listaAtributos(String nombreClase){
        ArrayList<String> listaAtributos = new ArrayList<>();
        try {
            clas = Class.forName(nombreClase);
            fiel = clas.getDeclaredFields();
            
            for (Field fie : fiel) {
                listaAtributos.add(fie.getName());
            }
            
        } catch (ClassNotFoundException c) {            
        }
        return listaAtributos;
    }
    
}
