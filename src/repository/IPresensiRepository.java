package repository;

public interface IPresensiRepository {
    void addPresensi(models.presensi.Presensi presensi);
    java.util.ArrayList<models.presensi.Presensi> getPresensiListByNIK(String nik);
}
