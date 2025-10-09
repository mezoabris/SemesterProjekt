package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class MediaHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
    /*
    TODO createMediaEntry(MediaRequest req) POST
    TODO deleteMediaEntry() DEL
    TODO updateMediaEntry(MediaRequest req) PUT
    TODO getMedia() GET List media entries
    TODO getMediaById(int Id) GET
    TODO deleteMedia(int Id) DELETE
    TODO updateMedia(int Id) PUT
    TODO getFilteredMedia( filters ) GET
     */
}
