package com.vswamy.abtest_client;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vswamy.ab_testing.ExperimentNotFoundException;
import com.vswamy.ab_testing.ExperimentService;
import com.vswamy.ab_testing.NullOrEmptyException;

public class JClient
{
    private static Logger logger = LoggerFactory.getLogger(JClient.class);

    TTransport transport;
    TProtocol protocol;
    ExperimentService.Client client = new ExperimentService.Client(protocol);

    JClient() throws TTransportException
    {
        transport = new TSocket("localhost", 9090);
        transport.open();
        protocol = new TBinaryProtocol(transport);
        client = new ExperimentService.Client(protocol);
    }

    private void pingTest() throws TException
    {
        logger.info("Starting ping test!...");

        long start = System.nanoTime();
        int t = 0;
        int f = 0;
        for (int i = 0; i < 10000; i++)
        {
            if (client.ping())
                t++;
            else
                f++;
        }
        long end = System.nanoTime();
        logger.info("True => {}", t);
        logger.info("False => {}", f);
        logger.info("Time taken in milliseconds for 10000 requests => {}", (end - start) / 1000000.0);
        return;
    }

    private void getExperimentStateTest() throws ExperimentNotFoundException, NullOrEmptyException, TException
    {
    	logger.info("Starting getExperimentState() test!...");
    	
        int c = 0;
        int t = 0;
        int e = 0;
        long start = System.nanoTime();
        for (int i = 0; i < 10000; i++)
        {
            String state = client.getExperimentState("experiment1");
            if (state.compareTo("C") == 0)
                c++;
            else if (state.compareTo("T") == 0)
                t++;
            else
                e++;
        }
        long end = System.nanoTime();
        logger.info("Time taken in milliseconds for 10000 requests => {}", (end - start) / 1000000.0);
        logger.info("t => {}", t);
        logger.info("c => {}", c);
        logger.info("e => {}", e);
    }

    public static void main(String[] args) throws TException
    {

        logger.info("client is starting...");

        JClient jclient = new JClient();
        jclient.pingTest();
        jclient.getExperimentStateTest();
        
        return;
    }
}
