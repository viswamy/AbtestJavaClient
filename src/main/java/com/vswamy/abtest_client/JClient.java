package com.vswamy.abtest_client;

import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vswamy.ab_testing.Experiment;
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

    private void getExperimentsStateTest() throws NullOrEmptyException, TException
    {
        logger.info("Starting getExperimentsState() test!...");
        LinkedList<String> list = new LinkedList<String>();
        list.add("experiment1");
        list.add("experiment2");

        int xc = 0;
        int xt = 0;
        int xe = 0;

        int yc = 0;
        int yt = 0;
        int ye = 0;
        long start = System.nanoTime();
        for (int i = 0; i < 10000; i++)
        {
            Map<String, String> output = client.getExperimentsState(list);
            String x = output.get("experiment1");
            String y = output.get("experiment2");
            if (x != null)
                if (x.compareTo("C") == 0)
                    xc++;
                else if (x.compareTo("T") == 0)
                    xt++;
                else
                    xe++;

            if (y != null)
                if (y.compareTo("C") == 0)
                    yc++;
                else if (y.compareTo("T") == 0)
                    yt++;
                else
                    ye++;
        }
        long end = System.nanoTime();

        logger.info("Time taken in milliseconds for 10000 requests => {}", (end - start) / 1000000.0);
        logger.info("experiment1 t => {}", xt);
        logger.info("experiment1 c => {}", xc);
        logger.info("experiment1 e => {}", xe);

        logger.info("experiment2 t => {}", yt);
        logger.info("experiment2 c => {}", yc);
        logger.info("experiment2 e => {}", ye);

    }

    public static void main(String[] args) throws TException
    {

        logger.info("client is starting...");

        JClient jclient = new JClient();

        // jclient.pingTest();

        // jclient.getExperimentStateTest();

        // jclient.getExperimentsStateTest();

        jclient.getExperimentTest();
        return;
    }

    private void getExperimentTest() throws ExperimentNotFoundException, NullOrEmptyException, TException
    {
        logger.info("Starting getExperiment() test!...");
        Experiment experiment[] = new Experiment[10000];
        long start = System.nanoTime();
        for (int i = 0; i < 10000; i++)
            experiment[i] = client.getExperiment("experiment1");

        long end = System.nanoTime();

        logger.info("Time taken in milliseconds for 10000 requests => {}", (end - start) / 1000000.0);
        logger.info("Experiment Author name => {}", experiment[0].getAuthorName());
        logger.info("Experiment Author Email Address => {}", experiment[0].getAuthorEmailAddress());
        logger.info("Experiment passcode => {}", experiment[0].getPasscode());

        StringBuilder builder = new StringBuilder();
        for (Entry<String, Integer> entry : experiment[0].getStateWeights().entrySet())
        {
            builder.append(entry.getKey() + "=>" + entry.getValue() + "\n");
        }
        logger.info("Experiment states => {}", builder.toString());
    }
}
