package android.vn.leo.qrscan.helper.parser;

import android.support.annotation.IntDef;
import android.util.SparseArray;
import android.vn.leo.qrscan.data.qrcode.ContactInfo;
import android.vn.leo.qrscan.data.qrcode.QRCodeBaseData;
import android.vn.leo.qrscan.interfaces.IQRCodeParser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class ContactParser implements IQRCodeParser {

    private final SparseArray<String> dataSet = new SparseArray<>();

    @IntDef({
            KeySet.FirstName, KeySet.LastName, KeySet.MobileNumber, KeySet.PhoneNumber,
            KeySet.FaxNumber, KeySet.MailAddress, KeySet.Company,
            KeySet.Job, KeySet.Address, KeySet.Website
    })@Retention(RetentionPolicy.SOURCE)
    @interface KeySet {
        int FirstName = 0;
        int LastName = 1;
        int MobileNumber = 2;
        int PhoneNumber = 3;
        int FaxNumber = 4;
        int MailAddress = 5;
        int Company = 6;
        int Job = 7;
        int Address = 8;
        int Website = 9;
    }

    @Override
    public QRCodeBaseData parse(String code) {
        // Clear cache
        release();

        // Create a new contact information object
        ContactInfo contactInfo = new ContactInfo();

        // Get array data from code with '\n' split
        String[] dataArr = code.split("\n");

        // Handle with every element of array data
        for (String element : dataArr) {
            try {
                handle(element);
            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }

        // Get the value of data after handle and set it to contact info
        contactInfo.setFirstName(get(KeySet.FirstName));
        contactInfo.setLastName(get(KeySet.LastName));
        contactInfo.setMobileNumber(get(KeySet.MobileNumber));
        contactInfo.setPhoneNumber(get(KeySet.PhoneNumber));
        contactInfo.setFaxNumber(get(KeySet.FaxNumber));
        contactInfo.setMailAddress(get(KeySet.MailAddress));
        contactInfo.setCompany(get(KeySet.Company));
        contactInfo.setJob(get(KeySet.Job));
        contactInfo.setWebsite(get(KeySet.Website));
        // Address
        String address = get(KeySet.Address);
        if (!address.isEmpty()) {
            String[] arr = address.split("&&");
            ContactInfo.Address address1 = new ContactInfo().new Address();
            if (arr.length >= 1) {
                address1.setStreet(arr[0]);
            }
            if (arr.length >= 2) {
                address1.setCity(arr[1]);
            }
            if (arr.length >= 3) {
                address1.setState(arr[2]);
            }
            if (arr.length >= 4) {
                address1.setPostcode(arr[3]);
            }
            if (arr.length >= 5) {
                address1.setCountry(arr[4]);
            }
            contactInfo.setAddress(address1);
        } else {
            contactInfo.setAddress(new ContactInfo().new Address());
        }

        // Return the value after set data
        return contactInfo;
    }

    private void handle(String element) throws IndexOutOfBoundsException {
        if (element == null || element.isEmpty()) {
            return;
        }
        if (element.startsWith("N:")) {
            String fullName = element.substring(element.lastIndexOf(":") + 1);
            if (fullName.contains(";")) {
                String[] arr = fullName.split(";");
                if (arr.length >= 2) {
                    put(KeySet.LastName, arr[0]);
                    put(KeySet.FirstName, arr[1]);
                }
            } else {
                put(KeySet.FirstName, fullName);
            }
        } else if (element.startsWith("FN:")) {
            String fullName = element.substring(element.lastIndexOf(":") + 1);
            if (fullName.contains(" ")) {
                String[] arr = fullName.split(" ");
                if (arr.length >= 2) {
                    put(KeySet.FirstName, arr[0]);
                    put(KeySet.LastName, arr[1]);
                } else if (arr.length == 1) {
                    put(KeySet.FirstName, arr[0]);
                }
            } else {
                put(KeySet.FirstName, fullName);
            }
        } else if (element.startsWith("ORG:")) {
            String company = element.substring(element.lastIndexOf(":") + 1);
            put(KeySet.Company, company);
        } else if (element.startsWith("TITLE:")) {
            String job = element.substring(element.lastIndexOf(":") + 1);
            put(KeySet.Job, job);
        } else if (element.startsWith("ADR:")) {
            String data = element.substring(element.lastIndexOf(":") + 1);
            if (data.contains(";")) {
                String[] addrs = data.split(";");
                String[] address = {"", "", "", "", ""};

                if (addrs.length >= 3) {
                    address[0] = addrs[2];
                }
                if (addrs.length >= 4) {
                    address[1] = addrs[3];
                }
                if (addrs.length >= 5) {
                    address[2] = addrs[4];
                }
                if (addrs.length >= 6) {
                    address[3] = addrs[5];
                }
                if (addrs.length >= 7) {
                    address[4] = addrs[6];
                }

                String addressEnd = address[0] + "&&"
                        + address[1] + "&&" + address[2] +"&&" + address[3] +"&&" + address[4];
                put(KeySet.Address, addressEnd);
            }
        } else if (element.startsWith("TEL;WORK;VOICE:") || element.startsWith("TEL:")) {
            String phoneNumber = element.substring(element.lastIndexOf(":") + 1);
            put(KeySet.PhoneNumber, phoneNumber);
        } else if (element.startsWith("TEL;CELL:") || element.startsWith("CELL:")) {
            String mobileNumber = element.substring(element.lastIndexOf(":") + 1);
            put(KeySet.MobileNumber, mobileNumber);
        } else if (element.startsWith("TEL;FAX:") || element.startsWith("FAX:")) {
            String faxNumber = element.substring(element.lastIndexOf(":") + 1);
            put(KeySet.FaxNumber, faxNumber);
        } else if (element.startsWith("EMAIL;WORK;INTERNET:")) {
            String email = element.substring(element.lastIndexOf(":") + 1);
            put(KeySet.MailAddress, email);
        } else if (element.startsWith("URL:")) {
            String website = element.substring(element.lastIndexOf(":") + 1);
            put(KeySet.Website, website);
        }
    }

    private void put(@KeySet int key, String value) {
        dataSet.put(key, value);
    }

    private String get(@KeySet int key) {
        return dataSet.get(key, "");
    }

    private void release() {
        dataSet.clear();
    }
}
