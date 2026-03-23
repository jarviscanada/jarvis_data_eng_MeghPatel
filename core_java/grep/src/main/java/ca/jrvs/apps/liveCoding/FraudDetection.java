package ca.jrvs.apps.liveCoding;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FraudDetection {

    public static List<Integer> detectFraud(List<Integer> transactions, int threshold) {

        List<Integer> result = new ArrayList<>();

        if (transactions == null || transactions.isEmpty()) {
            return result;
        }

        for (int t : transactions) {

            if (t > threshold) {
                result.add(t);
            }
        }
        return result;
    }

    public static List<Integer> detectFraudStream(List<Integer> transactions, int threshold) {

        if (transactions == null || transactions.isEmpty()) {
            return new ArrayList<>();
        }

        return transactions.stream()
                .filter(t -> t > threshold)
                .collect(Collectors.toList());
    }
}
