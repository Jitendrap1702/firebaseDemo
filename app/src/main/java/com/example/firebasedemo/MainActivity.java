package com.example.firebasedemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.firebasedemo.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.firestore.auth.User;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding b;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        Toast.makeText(MainActivity.this, "Created", Toast.LENGTH_SHORT).show();

//        addMultipleData();

//        addDataToFirebase();

//        getData();

//        deleteDocuments();

//        deleteFields();

//        transactions();

//        batchedWrites();

        performQueries();

    }
    private void addMultipleData() {
        /**Add Data**/
        // Create new user
        CollectionReference companies = db.collection("companies");

        Map<String, Object> company1 = new HashMap<>();
        company1.put("name", "Amazon");
        company1.put("CEO", "Jeff Bezos");
        company1.put("Headquarter", "Seattle, USA");
        company1.put("Revenue", "US$280.522 billion");
        company1.put("Headcount", "840,000 employees");
        companies.document("AMAZON").set(company1);

        Map<String, Object> company2 = new HashMap<>();
        company2.put("name", "Facebook");
        company2.put("CEO", "Mark Zuckerberg");
        company2.put("Headquarter", "Menlo Park, California, US.");
        company2.put("Revenue", "US$70.697 billion");
        company2.put("Headcount", "44,942+ employees");
        companies.document("FB").set(company2);

        Map<String, Object> company3 = new HashMap<>();
        company3.put("name", "Google");
        company3.put("CEO", "Sundar Pichai");
        company3.put("Headquarter", "Mountain View, California, USA");
        company3.put("Revenue", "US$66 billion");
        company3.put("Headcount", "114,096+ Employees");
        companies.document("GOOGLE").set(company3);

        Map<String, Object> company4 = new HashMap<>();
        company4.put("name", "Microsoft");
        company4.put("CEO", "Satya Nadella");
        company4.put("Headquarter", "Redmond, Washington, U.S.");
        company4.put("Revenue", "US$125.8 billion");
        company4.put("Headcount", "151,163+ Employees");
        companies.document("MS").set(company4);

        Map<String, Object> company5 = new HashMap<>();
        company5.put("name", "Adobe");
        company5.put("CEO", "Shantanu Narayen");
        company5.put("Headquarter", "San Jose, California, USA");
        company5.put("Revenue", " US$11.17 billion");
        company5.put("Headcount", "22,635 employees");
        companies.document("ADOBE").set(company5);

        db.collection("companies")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                            Toast.makeText(MainActivity.this, document.getId() + "=>" + document.getData(), Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(MainActivity.this, "Getting Error In Adding :" + task.getException(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        /**perform queries**/

        // Create a reference to the companies collection
        CollectionReference comRef = db.collection("companies");

        // Create a query against the collection
        Query query = comRef.whereEqualTo("name","Amdocs");

        Query capitalCities = db.collection("companies").whereEqualTo("name", true);

        db.collection("companies")
                .whereEqualTo("name", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Toast.makeText(MainActivity.this, document.getId() + "=>" + document.getData(), Toast.LENGTH_SHORT).show();
                                Log.d("LOG_TAG_1", document.getId() + "=>" + document.getData());
                            }
                        }else {
                            Toast.makeText(MainActivity.this, "Error Occured" + task.getException() , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Get data from cloud
    private void getData(){
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Toast.makeText(MainActivity.this, document.getId() + "=>" + document.getData(), Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(MainActivity.this, "etting Error Documents" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void addDataToFirebase(){
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Jitendra");
        user.put("last", "Patel");
        user.put("born", 1857);

// Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                       Toast.makeText(MainActivity.this, "DocumentSnapshot added with ID: " + documentReference.getId() , Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error adding document" + e , Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Delete documents
    private void deleteDocuments(){
        db.collection("companies").document("4b6CYnkTMkGej6C592m9")
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error Deleting Documents" + e, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //Delete fields
    private void deleteFields() {
        DocumentReference docRef = db.collection("companies").document("62l8gWZCiIdHpg88iITQ");

        // Remove the Headcount feild from documents
        Map<String, Object> updates = new HashMap<>();
        updates.put("Headcount", FieldValue.delete());

        docRef.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Field Deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error Deleting Documents" + e, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void transactions(){
        final DocumentReference sfDocRef = db.collection("users").document("2FLGtMEHIUiuvKf3rhaO");
        db.runTransaction(new Transaction.Function<Double>() {
            @Nullable
            @Override
            public Double apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sfDocRef);
                Double newBorn = snapshot.getDouble("born") + 1;
                if (newBorn <= 1860){
                    transaction.update(sfDocRef, "born", newBorn);
                    return newBorn;
                } else {
                    throw new FirebaseFirestoreException("Age too low",
                            FirebaseFirestoreException.Code.ABORTED);
                }
            }
        })
                .addOnSuccessListener(new OnSuccessListener<Double>() {
                    @Override
                    public void onSuccess(Double aDouble) {
                        Toast.makeText(MainActivity.this, "Transaction Success" + aDouble, Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Transaction Failure" + e, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void batchedWrites(){

        // Get a new write batch
        WriteBatch batch = db.batch();

        // Set the value
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Dinesh");
        user.put("last", "Patel");
        user.put("born", 1999);

        DocumentReference nycRef = db.collection("users").document("YnIopr89iwynAuJHhB8l");
        batch.set(nycRef, user);

        // Update the name of user "koykIAs8LYGV9ER3659u"
        DocumentReference sfRef = db.collection("users").document("koykIAs8LYGV9ER3659u");
        batch.update(sfRef, "name","Jstar" );

        // Delete the user "MnMqarnqeBzOSHbYaAPs"
        DocumentReference laRef = db.collection("users").document("MnMqarnqeBzOSHbYaAPs");
        batch.delete(laRef);

        // Commit the batch
        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error Occured :" + e, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    // Perform queries
    private void performQueries(){
       db.collection("companies")
               .whereEqualTo("name", "Google")
               .get()
               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if(task.isSuccessful()){
                           for(QueryDocumentSnapshot document : task.getResult()){
                               Log.d("LOG_TAG_1", document.getId() + " => " + document.getData());
                               Toast.makeText(MainActivity.this, document.getId() + "=>" + document.getData(), Toast.LENGTH_SHORT).show();
                           }
                       }else {
                           Toast.makeText(MainActivity.this, " Error Getting Documents", Toast.LENGTH_SHORT).show();
                       }
                   }
               });
    }
}