package com.italohreis.medly.repositories.specs;

import com.italohreis.medly.models.Patient;
import com.italohreis.medly.models.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class PatientSpec {

    public static Specification<Patient> hasName(String name) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Patient> hasCpf(String cpf) {
        String cleanedCpf = cpf.replaceAll("[^0-9]", "");
        return (root, query, cb) -> cb.equal(root.get("cpf"), cleanedCpf);
    }

    public static Specification<Patient> hasEmail(String email) {
        return (root, query, cb) -> {
            Join<Patient, User> userJoin = root.join("user");
            return cb.like(cb.lower(userJoin.get("email")), "%" + email.toLowerCase() + "%");
        };
    }

    public static Specification<Patient> isUserActive() {
        return (root, query, cb) -> {
            Join<Patient, User> userJoin = root.join("user");
            return cb.isTrue(userJoin.get("active"));
        };
    }
}
