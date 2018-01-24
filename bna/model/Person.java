package bna.model;

import java.util.Calendar;

public class Person {

    private String name;
    private Calendar birthDate;
    private int yearsOldExitPension;
    private Calendar datePensionExit;

    public Person(final String name,
                  final Calendar birthDate,
                  final int yearsOldExitPension) {
        this.name = name;
        this.birthDate = birthDate;
        this.yearsOldExitPension = yearsOldExitPension;
        datePensionExit = (Calendar) birthDate.clone();
        datePensionExit.add(Calendar.YEAR, yearsOldExitPension);
    }

    public Calendar getBirthday() {
        return birthDate;
    }

    public void setBirthday(Calendar birthDay) {
        this.birthDate = birthDay;
    }

    public Calendar getDatePensionExit() {
        return datePensionExit;
    }

    int getAge() {
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) <= birthDate.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age;
    }

    public int getYearsOldExitPension() {
        return yearsOldExitPension;
    }

    public void setYearsOldExitPension(int yearsOldExitPension) {
        this.yearsOldExitPension = yearsOldExitPension;
        datePensionExit = (Calendar) birthDate.clone();
        datePensionExit.add(Calendar.YEAR, this.yearsOldExitPension);
    }
}
