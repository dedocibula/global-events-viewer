package edu.vt.dlrl.dao;

import edu.vt.dlrl.domain.WordFrequency;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: dedocibula
 * Created on: 27.3.2017.
 */
@Repository("in-memory-dao")
public class InMemoryDAOImpl implements GlobalEventsDAO {

    private final String[] words;

    public InMemoryDAOImpl() {
        words = SAMPLE_SEED.replaceAll("[^A-Za-z0-9 ]", "").split(" ");
    }

    @Override
    public List<WordFrequency> getWordFrequencies() {
        List<WordFrequency> wordFrequencies = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            wordFrequencies.add(new WordFrequency(words[(int)(Math.random() * words.length)],
                    10 + (int)(Math.random() * 60)));
        }
        return wordFrequencies;
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
}
