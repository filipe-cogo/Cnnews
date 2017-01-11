/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import it.sauronsoftware.feed4j.FeedIOException;
import it.sauronsoftware.feed4j.FeedParser;
import it.sauronsoftware.feed4j.FeedXMLParseException;
import it.sauronsoftware.feed4j.UnsupportedFeedException;
import it.sauronsoftware.feed4j.bean.Feed;
import it.sauronsoftware.feed4j.bean.FeedItem;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for crawling CNN news
 * 
 * @author filipe
 */
public class Crawl {
 
        /**
         * Return the most recent RSS feeds from CNN.com
         * 
         * @return most recently published CNN news via RSS feeds 
         * @throws FeedIOException
         * @throws FeedXMLParseException
         * @throws UnsupportedFeedException 
         */
        public String getMostRecentRSSFeeds() throws FeedIOException, FeedXMLParseException, UnsupportedFeedException{
            try {
                URL url = new URL("http://rss.cnn.com/rss/cnn_latest.rss");
                Feed feed = FeedParser.parse(url);
                
                StringBuilder sb = new StringBuilder();
                for (int i =0; i < feed.getItemCount(); i++){
                    FeedItem item = feed.getItem(i);
                    
                    sb.append("<title>" + item.getTitle() + "\n");
                    sb.append("Link: " + item.getLink() + "\n");
                    sb.append("Plain text description: " + item.getDescriptionAsText() + "\n");      
                    //sb.append(item.getDescriptionAsHTML());
      
                }
                
                return sb.toString();
            } catch (MalformedURLException ex) {
                Logger.getLogger(Crawl.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }
}
