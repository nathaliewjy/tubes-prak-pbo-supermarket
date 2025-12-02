package repository;

import java.util.UUID;

public interface IPresensiRepository {
    void addPresensi(models.presensi.Presensi presensi);
    java.util.ArrayList<models.presensi.Presensi> getPresensiListByNIK(String nik);
    // ini ditambah nathalie
    int countPresensi(UUID employeeID);

}
