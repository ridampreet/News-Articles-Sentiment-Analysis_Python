package org.example;

import com.mongodb.client.*;
import org.bson.Document;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class App 
{
    int canadaCount=0;
    int torontoCount=0;
    int monctonCount=0;
    int totalArticles=0;
    public static void main( String[] args )
    {
        App ap=new App();
        Map<String,Integer> count=new HashMap<>();
        MongoClient mongoClient = MongoClients.create();
        MongoDatabase database = mongoClient.getDatabase("myMongoNews");
        MongoCollection<Document> collection = database.getCollection("articles");
        FindIterable<Document> iterDoc = collection.find();

        Iterator it = iterDoc.iterator();
        while (it.hasNext()) {
            ap.totalArticles++;
            String news=it.next().toString();
            String[] content=news.split(",");

            ap.countCanada(content[2]);
            ap.countMoncton(content[2]);
            ap.countToronto(content[2]);

        }
         //total articles in my mongoDB.

        int n=ap.totalArticles;
        float m= ap.canadaCount;
        double x=(double)n/(double)m;

        System.out.println("Search Query\t|\tDocument containing term df\t|\tTotal Docuemnts/number of documents term appeared\t|\tLog10(N/df)");
        System.out.println("Canada\t\t\t|\t\t"+""+ap.canadaCount+"\t\t\t\t\t\t|\t\t\t\t\t"+ap.totalArticles+"\t\t\t\t\t\t\t\t\t|\t"+Math.log10(x));
        m=ap.monctonCount;
        x=(double)n/(double)m;
        System.out.println("Moncton\t\t\t|\t\t"+""+ap.monctonCount+"\t\t\t\t\t\t|\t\t\t\t\t"+ap.totalArticles+"\t\t\t\t\t\t\t\t\t|\t"+Math.log10(x));
        m=ap.torontoCount;
        x=(double)n/(double)m;
        System.out.println("Toronto\t\t\t|\t\t"+""+ap.torontoCount+"\t\t\t\t\t\t|\t\t\t\t\t"+ap.totalArticles+"\t\t\t\t\t\t\t\t\t|\t"+Math.log10(x));



        iterDoc = collection.find();
        it = iterDoc.iterator();
        ap.getfm(it);
    }

    private void getfm(Iterator it) {
        int size=0;
        Map<String,Double> artVSfreq=new HashMap<>(); //to store the list of
        while(it.hasNext()){

            size=0;


            String s=it.next().toString().trim();
            s=s.replace("content=",""); //removing the word "content"
            String[] content=s.split(",");
            String[] arr=content[2].split(" ");
            int countCanada=0;
            artVSfreq.put(content[2],0.0);
            for(int i=0;i<arr.length;i++){  //iterate the article.
                arr[i]=arr[i].trim(); //remove spaces.
                if(!arr[i].isEmpty() && arr[i].length()>=1){
                    size++;

                    if(arr[i].contains("Canada")||arr[i].equals("Canada")){
                        countCanada++;

                    }
                }

            }
            double curr=artVSfreq.get(content[2]);
            curr=curr+countCanada;
            double rescurr=(double)curr/(double)size;
            artVSfreq.put(content[2],rescurr);



        }

        System.out.println();
        System.out.println();
        Double maxFreq=0.0;
        for (Map.Entry<String, Double> entry : artVSfreq.entrySet()) {
            if(entry.getValue()>maxFreq){
                maxFreq=entry.getValue();
            }

        }

        for (Map.Entry<String, Double> entry : artVSfreq.entrySet()) {
            if(entry.getValue()==maxFreq){
                String res=entry.getKey().replace(" r ","");
                res=res.replace(" nA"," A");
                res=res.trim();
                System.out.println("The maximum frequency by the formula of (f/m) is: "+entry.getValue() + "\nArticle: " + res);
            }

        }
    }

    public void countCanada(String content){
        String word="Canada";
        if(content.contains(word)||content.contains("CANADA")){
            canadaCount++;}
    }

    public void countMoncton(String content){
        String word="Moncton";
        if(content.contains(word)||content.contains("MONCTON")){monctonCount++;}
    }
    public void countToronto(String content){
        String word="Toronto";
        if(content.contains(word)||content.contains("TORONTO")){torontoCount++;}
    }

}
