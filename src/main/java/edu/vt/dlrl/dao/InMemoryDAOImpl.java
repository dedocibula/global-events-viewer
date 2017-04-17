package edu.vt.dlrl.dao;

import edu.vt.dlrl.domain.*;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Author: dedocibula
 * Created on: 27.3.2017.
 */
@Repository("in-memory-dao")
public class InMemoryDAOImpl implements GlobalEventsDAO {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyMMdd");
    private static final SortedMap<Date, InMemoryDataRow> IN_MEMORY_DB = new TreeMap<>();

    @Override
    public DateRange getMaxDateRange() {
        return new DateRange(IN_MEMORY_DB.firstKey(), IN_MEMORY_DB.lastKey());
    }

    @Override
    public LinkedHashMap<Event, List<TermFrequency>> getEventTermFrequencies(DateRange dateRange, int kForEvent) {
        LinkedHashMap<Event, List<TermFrequency>> eventTermFrequencies = new LinkedHashMap<>();
        for (InMemoryDataRow row : findMatchingRows(dateRange)) {
            int current = 0;
            List<TermFrequency> termFrequencies = new ArrayList<>();
            for (TermFrequency term : row.terms) {
                if (current >= kForEvent)
                    break;
                termFrequencies.add(new TermFrequency(term));
                current++;
            }
            eventTermFrequencies.put(new Event(row.id, row.eventName), termFrequencies);
        }
        return eventTermFrequencies;
    }

    @Override
    public LinkedHashMap<Event, List<String>> getEventTermToURLs(String term, DateRange dateRange) {
        LinkedHashMap<Event, List<String>> eventTermToURLs = new LinkedHashMap<>();
        for (InMemoryDataRow row : findMatchingRows(dateRange)) {
            if (row.mentions.containsKey(term))
                eventTermToURLs.put(new Event(row.id, row.eventName), new ArrayList<>(row.mentions.get(term)));
        }
        return eventTermToURLs;
    }

    @Override
    public List<Trend> getTrends(DateRange dateRange) {
        List<Trend> trends = new ArrayList<>();
        for (InMemoryDataRow row : findMatchingRows(dateRange))
            trends.add(new Trend(row.date, row.eventName, row.age, row.victims));
        return trends;
    }

    private Collection<InMemoryDataRow> findMatchingRows(DateRange dateRange) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateRange.getTo());
        cal.add(Calendar.YEAR, 1);
        return IN_MEMORY_DB.subMap(dateRange.getFrom(), cal.getTime()).values();
    }

    private static final String SAMPLE_SEED = "The Virginia Tech shooting, also known as the Virginia Tech massacre, occurred on April 16, 2007, " +
            "on the campus of Virginia Polytechnic Institute and State University in Blacksburg, Virginia, United States. " +
            "Seung-Hui Cho, a senior at Virginia Tech, shot and killed 32 people and wounded 17 others in two separate " +
            "attacks (another six people were injured escaping from classroom windows), approximately two hours apart, " +
            "before committing suicide. \n" +
            "The attacks received international media coverage and drew widespread criticism of U.S. gun culture. " +
            "It sparked intense debate about gun violence, gun laws, gaps in the U.S. system for treating mental health " +
            "issues, the perpetrator's state of mind, the responsibility of college administrations, privacy laws, " +
            "journalism ethics, and other issues. Television news organizations that aired portions of the killer's " +
            "multimedia manifesto were criticized by victims' families, Virginia law enforcement officials, and the " +
            "American Psychiatric Association. \n" +
            "Cho had previously been diagnosed with a severe anxiety disorder. During much of his middle school and " +
            "high school years, he received therapy and special education support. After graduating from high school, " +
            "Cho enrolled at Virginia Tech. Because of federal privacy laws, Virginia Tech was unaware of Cho's " +
            "previous diagnosis or the accommodations he had been granted at school. In 2005, Cho was accused of " +
            "stalking two female students. After an investigation, a Virginia special justice declared Cho mentally ill " +
            "and ordered him to attend treatment; however, because he was not institutionalized, he was still allowed " +
            "to purchase guns. The shooting prompted the state of Virginia to close legal loopholes that had " +
            "previously allowed individuals adjudicated as mentally unsound to purchase handguns without detection " +
            "by the National Instant Criminal Background Check System (NICS). It also led to passage of the only major " +
            "federal gun control measure in the U.S. since the year 1994. The law strengthening the NICS was signed by " +
            "President George W. Bush on January 5, 2008. \n" +
            "The Virginia Tech Review Panel, a state-appointed body assigned to review the incident, criticized " +
            "Virginia Tech administrators for failing to take action that might have reduced the number of casualties. " +
            "The panel's report also reviewed gun laws and pointed out gaps in mental health care as well as privacy " +
            "laws that left Cho's deteriorating condition in college untreated";

    private static final String[] SAMPLE_URLS = {
            "https://www.example.com/bubble.html",
            "http://www.example.edu/belief.html?basket=box&bubble=breath",
            "http://example.com/bike?bubble=amount&aftermath=acoustics",
            "http://www.example.net/baseball?advertisement=behavior",
            "https://www.example.com/#books",
            "https://www.example.com/",
            "https://www.example.org/",
            "http://example.com/",
            "https://www.example.com/",
            "http://www.example.org/bikes.php",
            "http://www.example.net/arithmetic/aunt.html",
            "http://www.example.net/?bottle=badge&basket=act",
            "https://www.example.org/apparel",
            "https://www.example.org/",
            "https://www.example.com/?bubble=attraction",
            "http://www.example.com/?beginner=authority",
            "http://example.edu/",
            "http://www.example.net/bat/birthday",
            "http://www.example.org/bead/airplane.php",
            "http://www.example.com/",
            "http://www.example.net/",
            "https://www.example.com/bag.aspx#airplane",
            "https://www.example.com/battle",
            "https://www.example.com/art.php",
            "https://www.example.com/",
            "http://example.edu/bomb/arch",
            "http://brick.example.com/anger/adjustment",
            "https://afternoon.example.com/",
            "https://account.example.org/babies.htm?authority=bee&angle=bead",
            "https://www.example.com/blade",
            "http://www.example.com/back/ants",
            "https://example.com/",
            "https://www.example.com/",
            "https://addition.example.com/act#boot",
            "https://attack.example.net/",
            "https://attack.example.com/",
            "https://example.com/baseball",
            "http://berry.example.com/?arm=believe",
            "http://example.com/",
            "https://www.example.net/?approval=book&attraction=achiever",
            "https://example.com/arm",
            "https://www.example.com/?badge=anger&ball=basket",
            "http://example.com/?amount=advertisement&animal=alarm",
            "https://example.com/",
            "https://www.example.com/",
            "http://example.org/",
            "http://example.com/apparatus/acoustics.aspx?bikes=actor&actor=beginner",
            "http://www.example.com/",
            "http://example.com/",
            "http://back.example.com/",
            "https://example.com/",
            "https://example.com/",
            "https://apparatus.example.com/blood.php?blow=apparatus&arithmetic=boy",
            "http://example.edu/bear.html",
            "https://www.example.com/?bead=apparel&boot=boat",
            "https://www.example.com/?branch=acoustics",
            "https://example.com/brake/agreement.html",
            "https://bubble.example.com/books?appliance=acoustics&amusement=activity",
            "http://www.example.com/bait.aspx",
            "https://example.com/?battle=ball",
            "https://www.example.com/babies.html",
            "http://example.com/adjustment/aftermath.html#birth",
            "https://adjustment.example.com/border.php",
            "https://www.example.com/?beginner=babies&act=basket",
            "http://example.com/actor/breath.html?birthday=baby#brass",
            "http://example.org/beginner/babies#argument",
            "http://example.com/",
            "http://www.example.com/?behavior=basketball",
            "https://example.edu/#boy",
            "http://beginner.example.com/bottle.html",
            "https://www.example.com/bike/back.html",
            "https://arch.example.com/approval/animal.html?adjustment=art",
            "http://example.com/",
            "http://brake.example.edu/basketball.htm",
            "http://www.example.com/board/bottle",
            "https://birth.example.com/bubble?bird=back#beef",
            "http://example.com/",
            "https://www.example.com/anger/boot.html",
            "http://birth.example.edu/brass",
            "http://basketball.example.com/basket",
            "https://airplane.example.com/",
            "https://www.example.com/",
            "http://www.example.com/",
            "https://example.com/bomb?air=berry",
            "https://example.com/bird.html",
            "http://www.example.net/",
            "http://www.example.com/",
            "http://www.example.com/",
            "http://www.example.com/agreement#aftermath",
            "https://www.example.com/acoustics",
            "https://www.example.edu/",
            "https://books.example.net/authority/brass",
            "https://example.net/",
            "http://example.com/",
            "https://www.example.net/ants",
            "https://example.com/#action",
            "https://www.example.com/",
            "http://www.example.com/brake/book",
            "https://www.example.com/boy/brick",
            "http://www.example.edu/boundary/birds",
            "https://www.example.com/authority/believe",
            "https://example.com/#aftermath",
            "http://behavior.example.org/acoustics.html",
            "https://example.edu/?birthday=apparatus",
            "http://example.com/#branch",
            "https://bed.example.org/arch.html?bomb=alarm&bear=bedroom",
            "http://www.example.com/",
            "http://example.com/bikes/boy.php",
            "http://example.com/birthday#bikes",
            "http://example.com/",
            "http://www.example.com/afterthought.html",
            "http://example.com/",
            "https://www.example.com/aunt.php",
            "http://breath.example.com/beef/amusement.php",
            "http://addition.example.com/balance.html",
            "https://www.example.org/bird/berry",
            "https://act.example.org/apparatus/acoustics.html",
            "https://www.example.net/",
            "https://example.com/",
            "https://www.example.com/",
            "http://example.net/",
            "https://www.example.org/",
            "http://www.example.com/",
            "http://www.example.net/anger",
            "http://www.example.com/",
            "http://www.example.com/",
            "https://www.example.com/#art",
            "http://argument.example.com/birth/bedroom#arm",
            "http://www.example.org/?ants=board",
            "https://example.org/?bag=beef",
            "http://example.com/bubble",
            "http://www.example.com/",
            "http://www.example.com/agreement.html?branch=bed&blood=behavior",
            "https://airport.example.com/book.aspx",
            "https://board.example.com/",
            "http://www.example.com/ants.html",
            "http://example.com/basketball/board",
            "http://www.example.com/bird.html",
            "http://actor.example.com/advice/bottle.php",
            "https://www.example.com/beginner/basketball.htm",
            "https://example.com/",
            "http://www.example.com/",
            "https://www.example.com/bike",
            "http://believe.example.com/",
            "https://example.com/agreement.html",
            "http://www.example.com/?bottle=advice",
            "http://example.com/board/bird.aspx",
            "http://www.example.edu/arithmetic/actor.aspx",
            "https://bone.example.com/apparel.html",
            "http://www.example.com/#bike",
            "http://bedroom.example.com/#book",
            "http://boy.example.com/",
            "https://www.example.com/believe/belief",
            "https://example.org/",
            "http://apparatus.example.com/blood/bear",
            "http://www.example.com/bear/brass.html?bird=attraction&boy=amusement",
            "https://example.com/",
            "http://www.example.com/believe.html",
            "https://example.com/arch.html",
            "http://example.net/bite/blade",
            "http://example.org/bone.html#belief",
            "https://ants.example.net/#aunt",
            "http://www.example.com/",
            "http://www.example.com/?back=back&behavior=acoustics#bead",
            "https://www.example.org/bait/base",
            "http://bikes.example.com/amount.html",
            "http://brass.example.com/believe/bottle.aspx?advertisement=beef&amusement=bikes#bomb",
            "https://example.edu/",
            "http://bit.example.com/",
            "https://www.example.com/",
            "http://www.example.com/back/airplane.php#berry",
            "https://www.example.com/",
            "http://www.example.com/afterthought/behavior?berry=achiever&babies=arithmetic",
            "http://example.edu/?belief=acoustics",
            "http://box.example.net/",
            "https://www.example.com/",
            "http://example.org/badge/airport",
            "http://www.example.net/arch?amusement=aunt&amount=bird",
            "https://example.com/?border=berry&alarm=bed",
            "http://badge.example.com/apparel/bikes",
            "http://example.com/airport.php",
            "http://www.example.net/",
            "http://www.example.org/books/art.aspx?bridge=beef&boat=bite#acoustics",
            "https://appliance.example.com/",
            "https://example.com/",
            "http://example.com/",
            "http://example.com/arm.php#acoustics",
            "http://example.com/",
            "https://www.example.com/",
            "https://example.com/brake.html",
            "https://amusement.example.com/#bike",
            "http://example.com/?airplane=babies&advice=bath",
            "http://example.edu/bath?boy=bone&afternoon=account",
            "https://www.example.net/",
            "https://www.example.com/ball/bath",
            "http://www.example.com/boat/authority#argument",
            "http://baseball.example.com/bed",
            "http://www.example.com/birth/bomb.html",
            "https://www.example.com/birth",
            "http://www.example.com/?beds=bottle&birds=blade",
            "http://www.example.com/",
            "http://bit.example.org/activity.php",
            "https://www.example.com/",
            "http://www.example.com/bear/bag",
            "https://example.com/?birth=art&back=boundary",
            "https://example.com/attack/authority?bridge=afternoon",
            "https://www.example.edu/arch/aftermath",
            "http://example.org/actor/bed",
            "https://www.example.com/art",
            "http://example.com/boundary/breath.html",
            "http://bubble.example.com/",
            "https://www.example.net/bedroom.htm?angle=alarm&bone=bike",
            "https://example.com/badge/bottle.html",
            "http://www.example.org/birthday/amusement",
            "https://www.example.com/ball/back",
            "http://www.example.edu/bell.php?airport=arm&bed=bike",
            "http://www.example.org/?brick=advice&baby=arch",
            "https://www.example.com/berry.php#book",
            "https://www.example.net/bone/actor.php",
            "http://www.example.com/",
            "https://www.example.org/",
            "https://www.example.net/",
            "https://www.example.com/airport/behavior",
            "http://www.example.com/bag",
            "https://www.example.com/",
            "https://www.example.org/",
            "http://example.com/boot/art.php",
            "http://example.com/bit/activity.html",
            "https://www.example.com/brake",
            "https://www.example.com/",
            "https://www.example.com/bait/advice.aspx",
            "https://board.example.com/?art=airport#bag",
            "https://www.example.com/",
            "http://example.com/belief/bikes.html?base=airport",
            "http://example.org/bridge?basket=balance&bee=basketball",
            "https://www.example.com/",
            "http://babies.example.com/",
            "http://www.example.com/book/book#achiever",
            "https://www.example.com/bed.html",
            "http://bomb.example.com/activity/bedroom.html",
            "http://example.com/bell/apparatus",
            "http://brick.example.edu/",
            "https://www.example.com/bite",
            "https://argument.example.com/?attack=baseball#branch",
            "http://www.example.com/?anger=blow",
            "http://www.example.org/bee?birthday=birth&afterthought=bait",
            "https://example.com/",
            "http://www.example.com/?basketball=air",
            "http://www.example.com/arithmetic.html",
            "http://www.example.com/?addition=bite",
            "http://www.example.net/bird",
            "https://www.example.net/",
            "https://example.net/ball/bedroom",
            "http://example.com/#brass",
            "https://example.com/",
            "http://agreement.example.com/adjustment?airport=attraction#alarm",
            "http://www.example.com/board",
            "https://example.com/",
            "http://example.com/?account=airplane&book=air",
            "https://attack.example.com/#argument",
            "https://example.com/",
            "http://www.example.com/bed?bite=agreement&babies=bead",
            "http://www.example.com/account/achiever.php",
            "https://basket.example.net/baby/bag",
            "http://www.example.edu/aftermath/bubble",
            "https://www.example.com/bed.html",
            "http://birth.example.com/",
            "https://actor.example.com/#badge",
            "https://example.edu/",
            "http://brass.example.com/",
            "https://www.example.com/books/army#brake",
            "https://www.example.com/bait/babies",
            "https://www.example.com/",
            "http://bear.example.com/",
            "http://bridge.example.com/amusement/approval.html?afterthought=amount&approval=account",
            "https://example.com/bedroom?art=bite",
            "https://www.example.net/",
            "https://example.com/",
            "https://books.example.com/",
            "https://www.example.org/",
            "https://www.example.org/",
            "https://www.example.com/authority/arm.php",
            "http://birds.example.org/bomb.htm",
            "https://www.example.net/",
            "https://example.com/#adjustment",
            "http://example.com/bubble/advice?adjustment=apparel",
            "http://example.com/believe?angle=board&badge=badge#agreement",
            "http://www.example.com/adjustment.aspx#anger",
            "https://www.example.com/",
            "https://branch.example.com/",
            "https://activity.example.org/basket/alarm.php",
            "http://www.example.org/?believe=battle&advertisement=bone",
            "https://example.com/authority.php",
            "http://example.net/",
            "https://www.example.com/arithmetic",
            "http://www.example.com/birthday.htm#birth",
            "https://www.example.com/",
            "https://www.example.com/border",
            "http://example.com/?appliance=bone&brass=boundary",
            "http://www.example.com/bee/anger"
    };

    static {
        Calendar cal = Calendar.getInstance();
        cal.set(2010, Calendar.OCTOBER, 1);
        addShooting(cal.getTime(), "Alisas 2010");
        cal.set(2010, Calendar.NOVEMBER, 29);
        addShooting(cal.getTime(), "Marinette 2010");
        cal.set(2011, Calendar.MARCH, 25);
        addShooting(cal.getTime(), "Martinsville 2011");
        cal.set(2011, Calendar.DECEMBER, 8);
        addShooting(cal.getTime(), "Virginia Tech 2011");
        cal.set(2011, Calendar.OCTOBER, 24);
        addShooting(cal.getTime(), "Cape Fear 2011");
        cal.set(2012, Calendar.MARCH, 6);
        addShooting(cal.getTime(), "Episcopal School 2012");
        cal.set(2013, Calendar.JANUARY, 10);
        addShooting(cal.getTime(), "Taft 2013");
        cal.set(2013, Calendar.JANUARY, 12);
        addShooting(cal.getTime(), "Osborn 2013");
        cal.set(2014, Calendar.JANUARY, 14);
        addShooting(cal.getTime(), "Berrendo 2014");
        cal.set(2015, Calendar.MARCH, 30);
        addShooting(cal.getTime(), "Pershing 2015");
        cal.set(2016, Calendar.OCTOBER, 13);
        addShooting(cal.getTime(), "McKinley STEM 2016");
        cal.set(2016, Calendar.OCTOBER, 25);
        addShooting(cal.getTime(), "Sandy 2016");
        cal.set(2017, Calendar.APRIL, 10);
        addShooting(cal.getTime(), "San Bernardino 2017");
    }

    private static void addShooting(Date date, String eventName) {
        InMemoryDataRow row = new InMemoryDataRow(DATE_FORMAT.format(date), date, eventName, 18 + (int) (Math.random() * 10), 10 + (int) (Math.random() * 30));
        String[] words = SAMPLE_SEED.replaceAll("[^A-Za-z0-9 ]", "").split(" ");
        for (int i = 0; i < 100; i++)
            row.terms.add(new TermFrequency(words[(int) (Math.random() * words.length)], 10 + (int) (Math.random() * 30)));
        for (TermFrequency term : row.terms) {
            row.mentions.put(term.getTerm(), new ArrayList<String>());
            for (int i = 0; i < term.getFrequency(); i++)
                row.mentions.get(term.getTerm()).add(SAMPLE_URLS[(int) (Math.random() * SAMPLE_URLS.length)]);
        }
        IN_MEMORY_DB.put(date, row);
    }


    private static final class InMemoryDataRow {
        private String id;
        private Date date;
        private String eventName;
        private int age;
        private int victims;
        private SortedSet<TermFrequency> terms;
        private Map<String, List<String>> mentions;

        private InMemoryDataRow(String id, Date date, String eventName, int age, int victims) {
            this.id = id;
            this.date = date;
            this.eventName = eventName;
            this.age = age;
            this.victims = victims;
            terms = new TreeSet<>(new Comparator<TermFrequency>() {
                @Override
                public int compare(TermFrequency o1, TermFrequency o2) {
                    int result = Integer.compare(o2.getFrequency(), o1.getFrequency());
                    return result != 0 ? result : o1.getTerm().compareTo(o2.getTerm());
                }
            });
            mentions = new HashMap<>();
        }
    }
}
