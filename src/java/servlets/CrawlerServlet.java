/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import it.sauronsoftware.feed4j.FeedIOException;
import it.sauronsoftware.feed4j.FeedParser;
import it.sauronsoftware.feed4j.FeedXMLParseException;
import it.sauronsoftware.feed4j.UnsupportedFeedException;
import it.sauronsoftware.feed4j.bean.Feed;
import it.sauronsoftware.feed4j.bean.FeedItem;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author filipe
 */
@WebServlet("/Crawler")
public class CrawlerServlet extends HttpServlet {

    /**
     * Synchronizes the 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods. 
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if (action.equals("sync")){
            try {
                URL url = new URL("http://rss.cnn.com/rss/cnn_latest.rss");
                Feed feed = FeedParser.parse(url);
                
                StringBuilder sb = new StringBuilder();
                for (int i =0; i < feed.getItemCount(); i++){
                    FeedItem item = feed.getItem(i);
                    
                    sb.append("Title: " + item.getTitle() + "\n");
                    sb.append("Link: " + item.getLink() + "\n");
                    sb.append("Plain text description: " + item.getDescriptionAsText() + "\n");      
                    //sb.append(item.getDescriptionAsHTML());
      
                }
                
                response.setContentType("text/html");
                response.getWriter().print(sb);
            } catch (FeedIOException ex) {
                Logger.getLogger(CrawlerServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FeedXMLParseException ex) {
                Logger.getLogger(CrawlerServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedFeedException ex) {
                Logger.getLogger(CrawlerServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet for sync last CNN news through RSS feed";
    }// </editor-fold>

}
