package edu.eci.arsw.blacklistvalidator;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;



public class Thread_BlackList extends Thread {


    private static final int BLACK_LIST_ALARM_COUNT=5;
    
    private AtomicInteger ocurrencesCount ;
    private int checkedListsCount = 0;
    private String ip;
    private LinkedList<Integer> blackListOcurrences=new LinkedList<>();
    private int inicio;
    private int fin;
    

    HostBlacklistsDataSourceFacade skds = HostBlacklistsDataSourceFacade.getInstance();

    public Thread_BlackList(int inicio, int fin, String ip, AtomicInteger ocurrencesCount){
        this.ip = ip;
        this.inicio = inicio;
        this.fin = fin;
        this.ocurrencesCount = ocurrencesCount;
    }

    @Override
    public void run(){
        for (int i=inicio;i<fin && ocurrencesCount.get()<BLACK_LIST_ALARM_COUNT;i++){
            checkedListsCount++;
            
            if (skds.isInBlackListServer(i, ip)){
                
                blackListOcurrences.add(i);
                
                ocurrencesCount.incrementAndGet();
            }
        }

    }

    public LinkedList<Integer> getBlackList(){
        return blackListOcurrences;
    }

    public int getCheckedListsCount(){
        return checkedListsCount;
    }





}
