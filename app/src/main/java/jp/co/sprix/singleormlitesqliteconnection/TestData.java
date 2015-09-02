package jp.co.sprix.singleormlitesqliteconnection;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable
public class TestData {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    public int userId;

    @DatabaseField
    public String name;

    @DatabaseField
    public String gender;

    @DatabaseField
    public int age;

    public TestData(int userId, String name, String gender, int age) {
        this.userId = userId;
        this.name = name;
        this.gender = gender;
        this.age = age;
    }

    public TestData() {
    }
}
