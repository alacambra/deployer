package com.poolingpeople.deployer.infrastructure.boundary;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.Tag;
import com.amazonaws.services.route53.AmazonRoute53;
import com.amazonaws.services.route53.AmazonRoute53Client;
import com.amazonaws.services.route53.model.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by alacambra on 23.03.15.
 */
public class DnsRegister {

    String accessKey;
    String secretKey;

    public List<Instance> getInstances(){
        loadAWSCredentials();
        AmazonEC2 ec2 = new AmazonEC2Client(new BasicAWSCredentials(accessKey, secretKey));
        ec2.setRegion(Region.getRegion(Regions.EU_WEST_1));
        DescribeInstancesResult result = ec2.describeInstances();

        List<Instance> instances = result.getReservations().stream().map(res -> (Instance) res.getInstances().get(0)).collect(Collectors.toList());

        instances.stream().map(
                instance -> instance.getTags().stream()
                        .filter((Tag)tag -> !tag.getKey().equals("name"))
                        .map(tag -> tag.getValue()).findFirst().get()).forEach(System.out::println);

        return null;
    }

    public void createDnsRecords(){
        loadAWSCredentials();
        AmazonRoute53 route53 = new AmazonRoute53Client(new BasicAWSCredentials(accessKey, secretKey));

        ChangeResourceRecordSetsRequest changeResourceRecordSetsRequest =
                new ChangeResourceRecordSetsRequest().withChangeBatch(
                        new ChangeBatch(Arrays.asList(
                                new Change()
                                        .withAction("CREATE")
                                        .withResourceRecordSet(new ResourceRecordSet()
                                                .withName("somename.intern.poolingpeople.com.")
                                                .withTTL(10L)
                                                .withType(RRType.A)
                                                .withResourceRecords(new ResourceRecord().withValue("192.168.10.12")))                        ))
                                .withComment("my first test"))
                        .withHostedZoneId("Z2JU9P5RO02LI8")
                ;



        route53.changeResourceRecordSets(changeResourceRecordSetsRequest);
    }

    public void updateDnsRecord(){
        loadAWSCredentials();
        AmazonRoute53 route53 = new AmazonRoute53Client(new BasicAWSCredentials(accessKey, secretKey));

        ChangeResourceRecordSetsRequest changeResourceRecordSetsRequest =
                new ChangeResourceRecordSetsRequest().withChangeBatch(
                        new ChangeBatch(Arrays.asList(
                                new Change()
                                        .withAction("UPSERT")
                                        .withResourceRecordSet(new ResourceRecordSet()
                                                .withName("somename.intern.poolingpeople.com.")
                                                .withTTL(10L)
                                                .withType(RRType.A)
                                                .withResourceRecords(new ResourceRecord().withValue("192.168.10.120")))                        ))
                                .withComment("my first test"))
                        .withHostedZoneId("Z2JU9P5RO02LI8")
                ;



        route53.changeResourceRecordSets(changeResourceRecordSetsRequest);
    }


    /*
    @todo: extract to utils lib
     */
    private void loadAWSCredentials(){

        accessKey = System.getenv("aws-access-key");
        secretKey = System.getenv("aws-secret-key");

        if( accessKey != null && accessKey != null ) return;

        InputStream akStream = getClass().getClassLoader().getResourceAsStream("aws-access-key");
        InputStream skStream = getClass().getClassLoader().getResourceAsStream("aws-secret-key");

        if( akStream == null || skStream == null ){
            throw new RuntimeException("aws keys not found");
        }

        accessKey = streamToString(akStream);
        secretKey = streamToString(skStream);

    }

    private String streamToString(InputStream in)  {

        try {

            StringBuilder out = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            for (String line = br.readLine(); line != null; line = br.readLine()) {
                out.append(line);
            }

            br.close();
            return out.toString();

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    // get current instances
    // locate those that needs domain on R53
    // update existent records
    // create new records
    // remove old records

}
