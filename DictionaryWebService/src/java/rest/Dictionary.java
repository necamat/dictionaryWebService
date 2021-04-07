/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import service.DictionaryServiceImpl;


@Path("translate")
public class Dictionary {

    @Context
    private UriInfo context;

    DictionaryServiceImpl dsi = new DictionaryServiceImpl();
    
    public Dictionary() {
    }

    
    @GET
    @Path("{word}/{language1}/{language2}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getXml(@PathParam ("word") String word,
                         @PathParam ("language1") String language1,
                         @PathParam ("language2") String language2) {
       
        return dsi.translate(word, language1, language2);
    }

    
}
