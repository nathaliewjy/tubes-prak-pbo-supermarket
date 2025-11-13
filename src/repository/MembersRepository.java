package repository;
import java.lang.reflect.Member;
import java.util.ArrayList;

import models.users.Employee;
import models.users.Members;

public class MembersRepository {
    
    public Members findByPhone(String phone){
        Members employeeFound = null;
        // work here
        return employeeFound;
    }

    public void addMembers(Members m){
        // work here
    }

    public void deleteMembers(){
        //work here 
    }

    public ArrayList<Members> getAllMembers() {
        ArrayList<Members> MembersList = new ArrayList<>();
        //work here
        return MembersList;
    }
}
