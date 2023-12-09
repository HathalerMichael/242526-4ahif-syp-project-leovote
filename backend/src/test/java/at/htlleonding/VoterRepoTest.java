package at.htlleonding;

import at.htlleonding.control.VoterRepository;
import at.htlleonding.entity.Candidate;
import at.htlleonding.entity.Election;
import at.htlleonding.entity.Voter;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class VoterRepoTest {
    @Inject
    EntityManager entityManager;

    @Inject
    VoterRepository voterRepository;

    @Test
    @Transactional
    public void Election_Test_good() {
        //arrange
        Candidate candidate_winner = new Candidate("id1", "c1", "votedFor", "1a");
        Candidate candidate_loser = new Candidate("id2", "c2", "notVotedFor", "2b");
        List<Election> electionList = new ArrayList<>();
        List<Candidate> candidateList = new ArrayList<>();

        //act
        entityManager.persist(candidate_winner);
        entityManager.persist(candidate_loser);
        candidateList.add(candidate_winner);
        candidateList.add(candidate_loser);
        Election election1 = new Election(
                "TestElection1",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "TestType",
                candidateList
        );
        entityManager.persist(election1);
        electionList.add(election1);
        List<Voter> voterList_winner = voterRepository.createVotersForElection(10, electionList);
        for (Voter v : voterList_winner) {
            voterRepository.voteForCandidate(v, candidate_winner, election1);
        }
        List<Voter> voterList_loser = voterRepository.createVotersForElection(2, electionList);
        for (Voter v : voterList_winner) {
            voterRepository.voteForCandidate(v, candidate_loser, election1);
        }

        //assert
        assertThat(candidateList.size()).isEqualTo(2);
        System.out.println("CandidateList Size :) \n\n");
        for (int i = 0; i < candidateList.size(); i++) {
            assertThat(election1.getParticipatingCandidates().get(i)).isEqualTo(candidateList.get(i));
            System.out.println("Candidate " + i + " equal to electionCandidate :) \n\n");
        }
        //TODO: assertThat() votes von Candidate equal zu den abgegebenen votes (10) und (2)

        //after
        entityManager.remove(candidate_winner);
        entityManager.remove(candidate_loser);
        for (Voter v : voterList_winner) {
            entityManager.remove(v);
        }
        for (Voter v : voterList_loser) {
            entityManager.remove(v);
        }
        entityManager.remove(election1);
    }

    @Test
    @Transactional
    public void Election_Test_Candidate_not_in_Election() {
        //arrange
        Candidate candidate_notVotedFor = new Candidate("id1", "c1", "notVotedFor", "1a");
        Candidate candidate_notInElection = new Candidate("id2", "c2", "notInElection", "2b");
        List<Election> electionList = new ArrayList<>();
        List<Candidate> candidateList = new ArrayList<>();

        //act
        entityManager.persist(candidate_notVotedFor);
        entityManager.persist(candidate_notInElection);
        candidateList.add(candidate_notVotedFor);
        Election election1 = new Election(
                "TestElection2",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "TestType",
                candidateList
        );
        entityManager.persist(election1);
        electionList.add(election1);
        List<Voter> voterList = voterRepository.createVotersForElection(10, electionList);
        for (Voter v : voterList) {
            voterRepository.voteForCandidate(v, candidate_notInElection, election1);
        }

        //assert
        for (int i = 0; i < candidateList.size(); i++) {
            assertThat(election1.getParticipatingCandidates().get(i)).isEqualTo(candidateList.get(i));
            System.out.println("Candidate " + i + " equal to electionCandidate :) \n\n");
        }
        //TODO: assertThat() votes von candidate_notInElection sind 0 (oder gar nicht vorhanden)
        //TODO: assertThat() voters haben noch ihre votes (weil sie einen nicht verfügbaren candidate gevotet haben

        //after
        entityManager.remove(candidate_notVotedFor);
        entityManager.remove(candidate_notInElection);
        for (Voter v : voterList) {
            entityManager.remove(v);
        }
        entityManager.remove(election1);
    }

    @Test
    @Transactional
    public void Election_Test_Voter_in_no_Election() {
        //arrange
        Candidate candidate1 = new Candidate("id1", "c1", "notInElection", "1b");
        List<Candidate> candidateList = new ArrayList<>();

        //act
        entityManager.persist(candidate1);
        Election election1 = new Election(
                "TestElection2",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "TestType",
                candidateList
        );
        entityManager.persist(election1);
        List<Voter> voterList = voterRepository.createVotersForElection(10, new ArrayList<>());
        for (Voter v : voterList) {
            voterRepository.voteForCandidate(v, candidate1, election1);

        }

        //assert
        assertThat(candidate1).isNotIn(election1.getParticipatingCandidates());
        System.out.println("Candidate1 not in Election :) \n\n");

        //TODO: assertThat() votes von candidate1 sind 0 (oder gar nicht vorhanden)

        //after0
        entityManager.remove(candidate1);
        for (Voter v : voterList) {
            entityManager.remove(v);
        }
        entityManager.remove(election1);
    }
}