package com.vswamy.abtest_client;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vswamy.ab_testing.ExperimentService;
import com.vswamy.ab_testing.ExperimentStateRequest;
import com.vswamy.ab_testing.ExperimentStateResponse;

public class JClient
{
    private static Logger logger = LoggerFactory.getLogger(JClient.class);

    public static void main(String[] args)
    {
        try
        {
            logger.info("client is starting...");
            TTransport transport;
            transport = new TSocket("localhost", 9090);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            ExperimentService.Client client = new ExperimentService.Client(protocol);

            ExperimentStateRequest request = new ExperimentStateRequest();
            request.setExperimentName("experiment1");
            logger.info("pinging server => {}", client.ping());
            int c = 0;
            int t = 0;
            int e = 0;
            long start = System.nanoTime();
            for (int i = 0; i < 10000; i++)
            {
                ExperimentStateResponse response = client.getExperimentState(request);
                if(response.getState().getName().compareTo("C") == 0) c++;
                else if (response.getState().getName().compareTo("T") == 0) t++;
                else
                    e++;
                // logger.info("The experiment name is {}",
                // response.experimentName);
                // logger.info("The experiment state is {}",
                // response.getState().getName());
            }
            long end = System.nanoTime();
            logger.info("time taken in seconds => {}", (end - start)/1000000.0);
            logger.info("t => {}", t);
            logger.info("c => {}", c);
            logger.info("e => {}", e);
            transport.close();
        }
        catch (TException x)
        {
            x.printStackTrace();
        }

    }
}
