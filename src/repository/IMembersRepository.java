package repository;

import models.users.Members;

import java.util.ArrayList;
import java.util.UUID;

public interface IMembersRepository {

    Members findByPhone(String phone);

    void addMembers(Members m);

    void deleteMembers(String phone);

    ArrayList<Members> getAllMembers();

    void updatePoints(UUID memberID, int addPoints);
}
