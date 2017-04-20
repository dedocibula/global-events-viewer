package edu.vt.dlrl.dao;

import edu.vt.dlrl.domain.DateRange;
import edu.vt.dlrl.domain.Event;
import edu.vt.dlrl.domain.TermFrequency;
import edu.vt.dlrl.domain.Trend;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.ResultsExtractor;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Author: dedocibula
 * Created on: 27.3.2017.
 */
public class HBaseDAOImpl implements GlobalEventsDAO {

    @Value("${hbase.table.name}")
    private String tableName;
    @Value("${hbase.table.cf}")
    private String columnFamily;
    @Value("${hbase.table.event.rowkeyprefix}")
    private String eventRowKeyPrefix;
    @Value("${hbase.table.event.name}")
    private String eventNameField;
    @Value("${hbase.table.event.date}")
    private String eventDateField;
    @Value("${hbase.table.event.dateformat}")
    private String eventDateFormat;
    @Value("${hbase.table.event.assailant.age}")
    private String eventAssailantAgeField;
    @Value("${hbase.table.event.victims.count}")
    private String eventVictimsCountField;
    @Value("${hbase.table.event.termseparator}")
    private String termSeparator;
    @Value("${hbase.table.event.urlseparator}")
    private String urlSeparator;
    @Value("${hbase.table.event.terms}")
    private String eventTermsField;
    @Value("${hbase.table.event.frequencies}")
    private String eventFrequenciesField;
    @Value("${hbase.table.event.urls}")
    private String eventURLsField;


    private final HbaseTemplate template;

    public HBaseDAOImpl(HbaseTemplate template) {
        this.template = template;
    }

    @Override
    public DateRange getMaxDateRange() {
        return template.find(tableName, columnFamily, eventDateField, new ResultsExtractor<DateRange>() {
            private SimpleDateFormat format = new SimpleDateFormat(eventDateFormat);

            @Override
            public DateRange extractData(ResultScanner results) throws Exception {
                byte[] firstDate = results.next().value();
                byte[] lastDate = firstDate;
                for (Result result : results)
                    lastDate = result.value();
                return new DateRange(format.parse(Bytes.toString(firstDate)), format.parse(Bytes.toString(lastDate)));
            }
        });
    }

    @Override
    public LinkedHashMap<Event, List<TermFrequency>> getEventTermFrequencies(DateRange dateRange, final int kForEvent) {
        final LinkedHashMap<Event, List<TermFrequency>> eventTermFrequencies = new LinkedHashMap<>();
        template.find(tableName, rangedScan(dateRange, eventNameField, eventTermsField, eventFrequenciesField), new ResultsExtractor<Void>() {
            @Override
            public Void extractData(ResultScanner results) throws Exception {
                for (Result result : results) {
                    List<TermFrequency> termFrequencies = new ArrayList<>();
                    String[] terms = getStringValue(result, eventTermsField).split(termSeparator);
                    String[] frequencies = getStringValue(result, eventFrequenciesField).split(termSeparator);
                    for (int i = 0; i < Math.min(terms.length, kForEvent); i++)
                        termFrequencies.add(new TermFrequency(terms[i], Integer.valueOf(frequencies[i])));
                    eventTermFrequencies.put(new Event(getRowKey(result), getStringValue(result, eventNameField)), termFrequencies);
                }
                return null;
            }
        });
        return eventTermFrequencies;
    }

    @Override
    public LinkedHashMap<Event, List<String>> getEventTermToURLs(final String term, DateRange dateRange) {
        final LinkedHashMap<Event, List<String>> eventTermToURLs = new LinkedHashMap<>();
        template.find(tableName, rangedScan(dateRange, eventNameField, eventTermsField, eventURLsField), new ResultsExtractor<Void>() {
            @Override
            public Void extractData(ResultScanner results) throws Exception {
                for (Result result : results) {
                    String[] terms = getStringValue(result, eventTermsField).split(termSeparator);
                    String[] urlLists = getStringValue(result, eventURLsField).split(termSeparator);
                    for (int i = 0; i < terms.length; i++) {
                        if (terms[i].equalsIgnoreCase(term))
                            eventTermToURLs.put(new Event(getRowKey(result), getStringValue(result, eventNameField)),
                                    Arrays.asList(urlLists[i].split(urlSeparator)));
                    }
                }
                return null;
            }
        });
        return eventTermToURLs;
    }

    @Override
    public List<Trend> getTrends(DateRange dateRange) {
        final List<Trend> trends = new ArrayList<>();
        template.find(tableName, rangedScan(dateRange, eventDateField, eventNameField, eventAssailantAgeField, eventVictimsCountField), new ResultsExtractor<Void>() {
            private SimpleDateFormat format = new SimpleDateFormat(eventDateFormat);

            @Override
            public Void extractData(ResultScanner results) throws Exception {
                for (Result result : results) {
                    trends.add(new Trend(format.parse(getStringValue(result, eventDateField)),
                            getStringValue(result, eventNameField),
                            getIntegerValue(result, eventAssailantAgeField),
                            getIntegerValue(result, eventVictimsCountField)));
                }
                return null;
            }
        });
        return trends;
    }

    private Scan rangedScan(DateRange dateRange, String... columnNames) {
        SimpleDateFormat format = new SimpleDateFormat(eventRowKeyPrefix);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateRange.getTo());
        cal.add(Calendar.YEAR, 1);
        Scan scan = new Scan(Bytes.toBytes(format.format(dateRange.getFrom())),
                Bytes.toBytes(format.format(cal.getTime())));
        for (String columnName : columnNames)
            scan.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columnName));
        return scan;
    }

    private String getRowKey(Result result) {
        return Bytes.toString(result.getRow());
    }

    private String getStringValue(Result result, String columnName) {
        return Bytes.toString(result.getValue(Bytes.toBytes(columnFamily), Bytes.toBytes(columnName)));
    }

    private int getIntegerValue(Result result, String columnName) {
        return Bytes.toInt(result.getValue(Bytes.toBytes(columnFamily), Bytes.toBytes(columnName)));
    }
}
