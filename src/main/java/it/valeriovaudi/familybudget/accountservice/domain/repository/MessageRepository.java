package it.valeriovaudi.familybudget.accountservice.domain.repository;

import org.reactivestreams.Publisher;

import java.util.Map;

public interface MessageRepository {

    Publisher<Map<String, String>> messages();
}
