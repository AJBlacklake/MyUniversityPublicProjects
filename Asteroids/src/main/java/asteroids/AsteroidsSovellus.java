package asteroids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AsteroidsSovellus extends Application{
    
    public static int LEVEYS = 600;
    public static int KORKEUS = 600;
    

    public static void main(String[] args) {
        Application.launch(args);
    }

    public static int osiaToteutettu() {
        // Ilmoita tämän metodin palautusarvolla kuinka monta osaa olet tehnyt
        return 4;
    }

    @Override
    public void start(Stage stage) {
        
         // Aloitus ruudun määrittäminen //
         
        BorderPane ruutu1 = new BorderPane();
        ruutu1.setPrefSize(LEVEYS, LEVEYS);
        
        VBox aloitusTekstit = new VBox();
        
        Label aloitusTeksti4 = new Label("Tehtäväsi on tuhota asteroideja"
                + " ja saada näin 50 pistetä");
        Label aloitusTeksti = new Label("Aloita peli painamalla Enteriä");
        Label aloitusTeksti2 = new Label("Voit liikkua nuolinäppäimistä");
        Label aloitusTeksti3 = new Label("voit ampua välilyönnistä");
        
        aloitusTekstit.setAlignment(Pos.CENTER);
        aloitusTekstit.setPadding(new Insets(50,50,50,50));
        aloitusTekstit.setSpacing(20);
        
        aloitusTekstit.getChildren().add(aloitusTeksti4);
        aloitusTekstit.getChildren().add(aloitusTeksti3);
        aloitusTekstit.getChildren().add(aloitusTeksti2);
        aloitusTekstit.getChildren().add(aloitusTeksti);
        
        
        ruutu1.setCenter(aloitusTekstit);
        
        // ----------------------------- //
        
        Pane ruutu2 = new Pane();
        ruutu2.setPrefSize(LEVEYS, KORKEUS);
        Text text = new Text(10, 20, "Points: 0");
        ruutu2.getChildren().add(text);
        
         // piste laskuri //
         
        AtomicInteger pisteet = new AtomicInteger();
        
        //---------------//
        
        // Lopetus ruudun määrittäminen //
         
        BorderPane ruutu3 = new BorderPane();
        ruutu3.setPrefSize(LEVEYS, LEVEYS);
        
        VBox loppuTekstit = new VBox();

        Label lopetusTeksti = new Label("Peli loppui! Pisteesi oli " + pisteet.intValue() );

        loppuTekstit.getChildren().add(lopetusTeksti);
        loppuTekstit.setAlignment(Pos.CENTER);

        ruutu3.setCenter(loppuTekstit);
        

        // ----------------------------- //
        
        
        // Aluksen ja Asteroidien luominen //
        
        Alus alus = new Alus(200, 200);
        List<Asteroidi> asteroidit = new ArrayList<>();
        for(int i = 0; i < 5 ; i++){
            Random rng = new Random();
            asteroidit.add(new Asteroidi(rng.nextInt(LEVEYS), rng.nextInt(KORKEUS)));
        }
        
        ruutu2.getChildren().add(alus.getHahmo());
        
        asteroidit.forEach(asteroidi -> ruutu2.getChildren().add(asteroidi.getHahmo()));
        
        // -------------------------------------------------- //
         
        // Ammukset // 
        
        List<Ammus> ammukset = new ArrayList<>();
        
        // ------- //
        
        //  Määritellään eri näkymät ja niille kyky tunnistaa napin painaminen //
        
        Scene scene1 = new Scene(ruutu1);
        Scene scene2 = new Scene(ruutu2);
        Scene scene3 = new Scene(ruutu3);
        
        Map<KeyCode, Boolean> painetutNapit = new HashMap<>();
        
        scene1.setOnKeyReleased((event) -> {
            painetutNapit.put(event.getCode(), Boolean.FALSE);
        });
        
        scene1.setOnKeyPressed((event) -> {
            painetutNapit.put(event.getCode(), Boolean.TRUE);
        });
        
        
        scene2.setOnKeyPressed((event) -> {
            painetutNapit.put(event.getCode(), Boolean.TRUE);
        });
        
        scene2.setOnKeyReleased((event) -> {
            painetutNapit.put(event.getCode(), Boolean.FALSE);
        });
        
        
        scene3.setOnKeyReleased((event) -> {
            painetutNapit.put(event.getCode(), Boolean.FALSE);
        });
        
        scene3.setOnKeyPressed((event) -> {
            painetutNapit.put(event.getCode(), Boolean.TRUE);
        });
        
        // ------------------------------------------------------------------- //
        
        
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                
                // määriellään pelin voitto ehto //
                
                if(voittaaPelin(pisteet.get())) {
                    stage.setScene(scene3);
                    stop();
                }
                
                // ----------------------------- //
                
                if(painetutNapit.getOrDefault(KeyCode.ENTER, false)) {
                        stage.setScene(scene2);
   
                }
                
                if(painetutNapit.getOrDefault(KeyCode.CONTROL, false)) {
                        stage.setScene(scene3);
                        stop();
   
                }
                
                if(painetutNapit.getOrDefault(KeyCode.ALT, false)) {
                        stage.setScene(scene2);
                        start();
                }

                //Asetetaan alukselle liikkeet napin painauksesta
                
                if(painetutNapit.getOrDefault(KeyCode.LEFT, false)){
                    alus.kaannaVasemmalle();
                }
                if(painetutNapit.getOrDefault(KeyCode.RIGHT, false)){
                    alus.kaannaOikealle();
                }
                if(painetutNapit.getOrDefault(KeyCode.UP, false)){
                    alus.kiihdyta();
                }
                if(painetutNapit.getOrDefault(KeyCode.SPACE, Boolean.FALSE) && ammukset.size() < 25){
                    Ammus ammus = new Ammus((int) alus.getHahmo().getTranslateX(), (int) alus.getHahmo().getTranslateY());
                    ammus.poistamisAjastin();
                    ammus.getHahmo().setRotate(alus.getHahmo().getRotate());
                    ammukset.add(ammus);
                    
                    ammus.kiihdyta();
                    ammus.setLiike(ammus.getLiike().normalize().multiply(3));
                    
                    ruutu2.getChildren().add(ammus.getHahmo());
                }
                
                //Asetetaan Hahmot liikkeelle
                
                alus.liiku();
                asteroidit.forEach(asteroidi -> asteroidi.liiku());
                ammukset.forEach(ammus -> ammus.liiku());
                
                asteroidit.forEach(asteroidi -> {
                    if(alus.tormaa(asteroidi)){
                    stop();
                    stage.setScene(scene3);
                    }
                    
  
                });
                // katotaan osuuko ammus
                
                ammukset.forEach(ammus -> {
                    asteroidit.forEach(asteroid -> {
                        if(ammus.tormaa(asteroid)) {
                            ammus.setElossa(false);
                            asteroid.setElossa(false);
                            text.setText("Pisteet: " + pisteet.addAndGet(1));
                            lopetusTeksti.setText("Peli loppui! Pisteesi oli " + pisteet);
                            if(voittaaPelin(pisteet.get())){
                                lopetusTeksti.setText("onneksi olkoon! Voitit pelin");
                        }
                        }
                    });
                                  
                });       
                
                //poistetaan ruudulta ja listalta ammukset jotka on osunut
                
                ammukset.stream()
                        .filter(ammus ->  !ammus.isElossa())
                        .forEach(ammus -> ruutu2.getChildren().remove(ammus.getHahmo()));
                ammukset.stream()
                        .filter(ammus -> !ammus.isElossa())
                        .forEach(ammus -> ammukset.remove(ammus));
                
                //poistetaan asteroidit joihin on osunut
                
                asteroidit.stream()
                            .filter(asteroidi2 -> !asteroidi2.isElossa())
                            .forEach(asteroidi2 -> ruutu2.getChildren().remove(asteroidi2.getHahmo()));
                    asteroidit.removeAll(asteroidit.stream()
                            .filter(asteroidi2 -> !asteroidi2.isElossa())
                            .collect(Collectors.toList()));
                 
                if(Math.random() < 0.01){
                    Asteroidi asteroidi = new Asteroidi(new Random().nextInt(LEVEYS), new Random().nextInt(KORKEUS));
                    if(!asteroidi.tormaa(alus)) {
                        asteroidit.add(asteroidi);
                        ruutu2.getChildren().add(asteroidi.getHahmo());
                    }
                }
                    
            } 
            
        };
        
        
        timer.start();
        
        
        stage.setTitle("Asteroids!");
        stage.setScene(scene1);
        stage.show();
        
    }
    
    public boolean voittaaPelin(int pisteet) {
        if(pisteet >= 50) {
            return true;
        }
        
        return false;
    }

}
