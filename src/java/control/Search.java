/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import it.sauronsoftware.feed4j.bean.Feed;
import it.sauronsoftware.feed4j.bean.FeedItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author filipe
 */
public class Search {
    private static List<String> indexedFeedsIds = new ArrayList<String>();
    private static Directory index = new RAMDirectory();

    /**
     * Index all feeds by 'id', 'title', 'link' and 'description' in a static index
     * 
     * @param feed Feeds to be indexed
     */
    protected void indexFeed(Feed feed){
        try {
            StandardAnalyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            IndexWriter w = new IndexWriter(index, config);
            
            for (int i =0; i < feed.getItemCount(); i++){
                FeedItem item = feed.getItem(i);
                
                if (!indexedFeedsIds.contains((String)item.getGUID())){
                    Document document = new Document();
                    document.add(new TextField("id", item.getGUID(), Field.Store.YES));
                    document.add(new TextField("title", item.getTitle(), Field.Store.YES));
                    document.add(new TextField("link", item.getLink().toString(), Field.Store.YES));
                    document.add(new TextField("description", item.getDescriptionAsText(), Field.Store.YES));
                    w.addDocument(document);
                    
                    indexedFeedsIds.add(item.getGUID());
                }
            }
            w.close();
        } catch (IOException ex) {
            Logger.getLogger(Search.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Search for previously indexed feeds through 'title' and 'description' fields, according to a query
     * 
     * @param query terms to be considered in the search
     * @return a JSON representation of the retrieved feeds
     * @throws ParseException query parsing failure
     * @throws IOException  I/O issue when creating index
     */
    public String queryIndexedFeeds(String query) throws ParseException, IOException{
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        StandardAnalyzer analyzer = new StandardAnalyzer();
        MultiFieldQueryParser queryParser = new MultiFieldQueryParser(
            new String[] {"title", "description"},
            analyzer);

        TopDocs docs = searcher.search(queryParser.parse(query), 25);
        ScoreDoc[] hits = docs.scoreDocs;
        
        JSONArray jsonArray = new JSONArray();
        JSONObject json = new JSONObject();
        for (int i = 0; i < hits.length; i++){            
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            
            json.put("id", d.get("id"));
            json.put("title", d.get("title"));
            json.put("link", d.get("link"));
            json.put("description", d.get("description"));
            
            jsonArray.put(json);
        }
        
        reader.close();        
        String ret = jsonArray.toString();
        return ret;
    }
}
