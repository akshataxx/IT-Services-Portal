package model.application;

import model.domain.*;
import util.CountMap;
import util.Preconditions;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class StatisticsReport {


    public StatisticsReport() {
    }

    public int getStressRate() {
        ITPortal portal = ITPortal.getInstance();
        int staffCount = 0;
        for(UserBean user : portal.getUserManager().getAllUsers()) {
            if(user.getRole().equals(UserRole.IT_STAFF))
                staffCount++;
        }

        int totalUnsolvedIncidents = 0;
        for(IssueBean issue : portal.getIssueManager().getAllIssues()) {
            if(!issue.isResolved())
                totalUnsolvedIncidents++;
        }

        return (totalUnsolvedIncidents)/(staffCount*5);
    }

    public CountMap<Category> unsolvedEachCategory() {
        CountMap<Category> countMap = new CountMap<>();
        ITPortal portal = ITPortal.getInstance();
        for(IssueBean issue : portal.getIssueManager().getAllIssues()) {
            if(!issue.isResolved()) {
                countMap.increment(issue.getCategory());
                countMap.increment(issue.getCategory().asMain());
            }
        }
        return countMap;
    }

    public CountMap<Category> solvedEachCategoryLastDays(int days) {
        Preconditions.checkArgument(days>0);
        CountMap<Category> countMap = new CountMap<>();
        ITPortal portal = ITPortal.getInstance();
        long daysMillis = Duration.of(days, ChronoUnit.DAYS).toMillis();
        for(IssueBean issue : portal.getIssueManager().getAllIssues()) {
            if(issue.isResolved()) {
                long lastSolved = System.currentTimeMillis() - issue.getResolveDateTime();
                if (lastSolved < daysMillis) {
                    countMap.increment(issue.getCategory());
                    countMap.increment(issue.getCategory().asMain());
                }
            }
        }
        return countMap;
    }


}
