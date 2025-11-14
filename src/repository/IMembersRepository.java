package repository;

import models.users.Members;

import java.util.ArrayList;

public interface IMembersRepository {

    Members findByPhone(String phone);

    void addMembers(Members m);

    void deleteMembers(String phone);

    ArrayList<Members> getAllMembers();
}
