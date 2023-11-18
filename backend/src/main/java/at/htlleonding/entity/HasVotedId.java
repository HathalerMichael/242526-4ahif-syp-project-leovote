package at.htlleonding.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;

@Embeddable
public class HasVotedId implements Serializable {
    //<editor-fold desc="Fields">
    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Election election;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Voter voter;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public HasVotedId() {

    }

    public HasVotedId(Election election, Voter voter) {
        this.election = election;
        this.voter = voter;
    }
    //</editor-fold>

    //<editor-fold desc="Getter and Setter">
    public Election getElection() {
        return election;
    }

    public void setElection(Election election) {
        this.election = election;
    }
    //</editor-fold>

    public Voter getVoter() {
        return voter;
    }

    public void setVoter(Voter voter) {
        this.voter = voter;
    }
}