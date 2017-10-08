package ru.krygin.smart_sight.patients.db;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import ru.krygin.smart_sight.examination.db.ExaminationData;

import static ru.krygin.smart_sight.patients.db.PatientData.TABLE_NAME_PATIENTS;

/**
 * Created by Ivan on 01.10.2017.
 */
@DatabaseTable(tableName = TABLE_NAME_PATIENTS)
public class PatientData {

    public static final String TABLE_NAME_PATIENTS = "patients";

    public static final String FIELD_ID = "id";
    public static final String FIELD_FIRST_NAME = "first_name";
    public static final String FIELD_LAST_NAME = "last_name";
    public static final String FIELD_PATRONYMIC = "patronymic";
    public static final String FIELD_DATE_OF_BIRTH = "date_of_birth";
    public static final String FIELD_GENDER = "gender";
    public static final String FIELD_CARD_NUMBER = "card_number";

    @DatabaseField(columnName = FIELD_ID, generatedId = true)
    private long UUID;

    @DatabaseField(columnName = FIELD_FIRST_NAME)
    private String firstName;

    @DatabaseField(columnName = FIELD_LAST_NAME)
    private String lastName;

    @DatabaseField(columnName = FIELD_PATRONYMIC)
    private String patronymic;

    @DatabaseField(columnName = FIELD_DATE_OF_BIRTH)
    private Date dateOfBirth;

    @DatabaseField(columnName = FIELD_GENDER)
    private Boolean gender; // true - male, false - female, null - undefinded

    @DatabaseField(columnName = FIELD_CARD_NUMBER)
    private String cardNumber;

    @ForeignCollectionField(eager = true)
    public ForeignCollection<ExaminationData> examinations;

    public long getUUID() {
        return UUID;
    }

    public void setUUID(long UUID) {
        this.UUID = UUID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public ForeignCollection<ExaminationData> getExaminations() {
        return examinations;
    }
}
