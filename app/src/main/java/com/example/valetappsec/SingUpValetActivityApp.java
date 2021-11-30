package com.example.valetappsec;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.valetappsec.Adapter.ListAdapterColor;
import com.example.valetappsec.Adapter.ListAdapterOrder;
import com.example.valetappsec.Json.ExportJson;
import com.example.valetappsec.Json.ImportJson;
import com.example.valetappsec.Model.CarType;
import com.example.valetappsec.Model.ClientOrder;
import com.example.valetappsec.Model.ColorCarModel;
import com.example.valetappsec.Model.SingUpClientModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import eltos.simpledialogfragment.SimpleDialog;
import eltos.simpledialogfragment.color.SimpleColorWheelDialog;

import static com.example.valetappsec.GlobalVairable.clientOrders;
import static eltos.simpledialogfragment.SimpleDialog.OnDialogResultListener.BUTTON_POSITIVE;

public class SingUpValetActivityApp extends AppCompatActivity implements View.OnClickListener , SimpleDialog.OnDialogResultListener  {

    Button next, singUp;
    EditText userName, phoneNo, password, email,
            /*carType, carModel,*/ carLot;
Spinner carTypeSpinner,carModelSpinner;
ArrayAdapter<String> carTAdapter,carMAdapter;
    LinearLayout sing_up_user_info, sing_up_car_info;
    private static final int SELECT_IMAGE = 3;
    private Uri fileUri;
    Bitmap YourPicBitmap1 = null;
    ImageView imagwPro;
    List<String> carTypeList;
    List<String> carMList;
    List<List<String>> carModelList;
    List<CarType>CarsTypes;
    TextView carYear,carColor;
    String carT,carM;
    private Calendar myCalendar;
    SimpleDateFormat sdf;
    List<String> clearS;
    String myFormat;
    TextView imageG,imageD,imageC;
    String COLOR_PICKER="34";
    List<ColorCarModel>carColorList;
    ListView colorList;
    ListAdapterColor listAdapterColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sing_up_layout);
        initialization();

        String json="[{\"brand\": \"Seat\", \"models\": [\"Alhambra\", \"Altea\", \"Altea XL\", \"Arosa\", \"Cordoba\", \"Cordoba Vario\", \"Exeo\", \"Ibiza\", \"Ibiza ST\", \"Exeo ST\", \"Leon\", \"Leon ST\", \"Inca\", \"Mii\", \"Toledo\"]},\n" +
                "{\"brand\": \"Renault\", \"models\": [\"Captur\", \"Clio\", \"Clio Grandtour\", \"Espace\", \"Express\", \"Fluence\", \"Grand Espace\", \"Grand Modus\", \"Grand Scenic\", \"Kadjar\", \"Kangoo\", \"Kangoo Express\", \"Koleos\", \"Laguna\", \"Laguna Grandtour\", \"Latitude\", \"Mascott\", \"Mégane\", \"Mégane CC\", \"Mégane Combi\", \"Mégane Grandtour\", \"Mégane Coupé\", \"Mégane Scénic\", \"Scénic\", \"Talisman\", \"Talisman Grandtour\", \"Thalia\", \"Twingo\", \"Wind\", \"Zoé\"]},\n" +
                "{\"brand\": \"Peugeot\", \"models\": [\"1007\", \"107\", \"106\", \"108\", \"2008\", \"205\", \"205 Cabrio\", \"206\", \"206 CC\", \"206 SW\", \"207\", \"207 CC\", \"207 SW\", \"306\", \"307\", \"307 CC\", \"307 SW\", \"308\", \"308 CC\", \"308 SW\", \"309\", \"4007\", \"4008\", \"405\", \"406\", \"407\", \"407 SW\", \"5008\", \"508\", \"508 SW\", \"605\", \"806\", \"607\", \"807\", \"Bipper\", \"RCZ\"]},\n" +
                "{\"brand\": \"Dacia\", \"models\": [\"Dokker\", \"Duster\", \"Lodgy\", \"Logan\", \"Logan MCV\", \"Logan Van\", \"Sandero\", \"Solenza\"]},\n" +
                "{\"brand\": \"Citroën\", \"models\": [\"Berlingo\", \"C-Crosser\", \"C-Elissée\", \"C-Zero\", \"C1\", \"C2\", \"C3\", \"C3 Picasso\", \"C4\", \"C4 Aircross\", \"C4 Cactus\", \"C4 Coupé\", \"C4 Grand Picasso\", \"C4 Sedan\", \"C5\", \"C5 Break\", \"C5 Tourer\", \"C6\", \"C8\", \"DS3\", \"DS4\", \"DS5\", \"Evasion\", \"Jumper\", \"Jumpy\", \"Saxo\", \"Nemo\", \"Xantia\", \"Xsara\"]},\n" +
                "{\"brand\": \"Opel\", \"models\": [\"Agila\", \"Ampera\", \"Antara\", \"Astra\", \"Astra cabrio\", \"Astra caravan\", \"Astra coupé\", \"Calibra\", \"Campo\", \"Cascada\", \"Corsa\", \"Frontera\", \"Insignia\", \"Insignia kombi\", \"Kadett\", \"Meriva\", \"Mokka\", \"Movano\", \"Omega\", \"Signum\", \"Vectra\", \"Vectra Caravan\", \"Vivaro\", \"Vivaro Kombi\", \"Zafira\"]},\n" +
                "{\"brand\": \"Alfa Romeo\", \"models\": [\"145\", \"146\", \"147\", \"155\", \"156\", \"156 Sportwagon\", \"159\", \"159 Sportwagon\", \"164\", \"166\", \"4C\", \"Brera\", \"GTV\", \"MiTo\", \"Crosswagon\", \"Spider\", \"GT\", \"Giulietta\", \"Giulia\"]},\n" +
                "{\"brand\": \"Škoda\", \"models\": [\"Favorit\", \"Felicia\", \"Citigo\", \"Fabia\", \"Fabia Combi\", \"Fabia Sedan\", \"Felicia Combi\", \"Octavia\", \"Octavia Combi\", \"Roomster\", \"Yeti\", \"Rapid\", \"Rapid Spaceback\", \"Superb\", \"Superb Combi\"]},\n" +
                "{\"brand\": \"Chevrolet\", \"models\": [\"Alero\", \"Aveo\", \"Camaro\", \"Captiva\", \"Corvette\", \"Cruze\", \"Cruze SW\", \"Epica\", \"Equinox\", \"Evanda\", \"HHR\", \"Kalos\", \"Lacetti\", \"Lacetti SW\", \"Lumina\", \"Malibu\", \"Matiz\", \"Monte Carlo\", \"Nubira\", \"Orlando\", \"Spark\", \"Suburban\", \"Tacuma\", \"Tahoe\", \"Trax\"]},\n" +
                "{\"brand\": \"Porsche\", \"models\": [\"911 Carrera\", \"911 Carrera Cabrio\", \"911 Targa\", \"911 Turbo\", \"924\", \"944\", \"997\", \"Boxster\", \"Cayenne\", \"Cayman\", \"Macan\", \"Panamera\"]},\n" +
                "{\"brand\": \"Honda\", \"models\": [\"Accord\", \"Accord Coupé\", \"Accord Tourer\", \"City\", \"Civic\", \"Civic Aerodeck\", \"Civic Coupé\", \"Civic Tourer\", \"Civic Type R\", \"CR-V\", \"CR-X\", \"CR-Z\", \"FR-V\", \"HR-V\", \"Insight\", \"Integra\", \"Jazz\", \"Legend\", \"Prelude\"]},\n" +
                "{\"brand\": \"Subaru\", \"models\": [\"BRZ\", \"Forester\", \"Impreza\", \"Impreza Wagon\", \"Justy\", \"Legacy\", \"Legacy Wagon\", \"Legacy Outback\", \"Levorg\", \"Outback\", \"SVX\", \"Tribeca\", \"Tribeca B9\", \"XV\"]},\n" +
                "{\"brand\": \"Mazda\", \"models\": [\"121\", \"2\", \"3\", \"323\", \"323 Combi\", \"323 Coupé\", \"323 F\", \"5\", \"6\", \"6 Combi\", \"626\", \"626 Combi\", \"B-Fighter\", \"B2500\", \"BT\", \"CX-3\", \"CX-5\", \"CX-7\", \"CX-9\", \"Demio\", \"MPV\", \"MX-3\", \"MX-5\", \"MX-6\", \"Premacy\", \"RX-7\", \"RX-8\", \"Xedox 6\"]},\n" +
                "{\"brand\": \"Mitsubishi\", \"models\": [\"3000 GT\", \"ASX\", \"Carisma\", \"Colt\", \"Colt CC\", \"Eclipse\", \"Fuso canter\", \"Galant\", \"Galant Combi\", \"Grandis\", \"L200\", \"L200 Pick up\", \"L200 Pick up Allrad\", \"L300\", \"Lancer\", \"Lancer Combi\", \"Lancer Evo\", \"Lancer Sportback\", \"Outlander\", \"Pajero\", \"Pajeto Pinin\", \"Pajero Pinin Wagon\", \"Pajero Sport\", \"Pajero Wagon\", \"Space Star\"]},\n" +
                "{\"brand\": \"Lexus\", \"models\": [\"CT\", \"GS\", \"GS 300\", \"GX\", \"IS\", \"IS 200\", \"IS 250 C\", \"IS-F\", \"LS\", \"LX\", \"NX\", \"RC F\", \"RX\", \"RX 300\", \"RX 400h\", \"RX 450h\", \"SC 430\"]},\n" +
                "{\"brand\": \"Toyota\", \"models\": [\"4-Runner\", \"Auris\", \"Avensis\", \"Avensis Combi\", \"Avensis Van Verso\", \"Aygo\", \"Camry\", \"Carina\", \"Celica\", \"Corolla\", \"Corolla Combi\", \"Corolla sedan\", \"Corolla Verso\", \"FJ Cruiser\", \"GT86\", \"Hiace\", \"Hiace Van\", \"Highlander\", \"Hilux\", \"Land Cruiser\", \"MR2\", \"Paseo\", \"Picnic\", \"Prius\", \"RAV4\", \"Sequoia\", \"Starlet\", \"Supra\", \"Tundra\", \"Urban Cruiser\", \"Verso\", \"Yaris\", \"Yaris Verso\"]},\n" +
                "{\"brand\": \"BMW\", \"models\": [\"i3\", \"i8\", \"M3\", \"M4\", \"M5\", \"M6\", \"Rad 1\", \"Rad 1 Cabrio\", \"Rad 1 Coupé\", \"Rad 2\", \"Rad 2 Active Tourer\", \"Rad 2 Coupé\", \"Rad 2 Gran Tourer\", \"Rad 3\", \"Rad 3 Cabrio\", \"Rad 3 Compact\", \"Rad 3 Coupé\", \"Rad 3 GT\", \"Rad 3 Touring\", \"Rad 4\", \"Rad 4 Cabrio\", \"Rad 4 Gran Coupé\", \"Rad 5\", \"Rad 5 GT\", \"Rad 5 Touring\", \"Rad 6\", \"Rad 6 Cabrio\", \"Rad 6 Coupé\", \"Rad 6 Gran Coupé\", \"Rad 7\", \"Rad 8 Coupé\", \"X1\", \"X3\", \"X4\", \"X5\", \"X6\", \"Z3\", \"Z3 Coupé\", \"Z3 Roadster\", \"Z4\", \"Z4 Roadster\"]},\n" +
                "{\"brand\": \"Volkswagen\", \"models\": [\"Amarok\", \"Beetle\", \"Bora\", \"Bora Variant\", \"Caddy\", \"Caddy Van\", \"Life\", \"California\", \"Caravelle\", \"CC\", \"Crafter\", \"Crafter Van\", \"Crafter Kombi\", \"CrossTouran\", \"Eos\", \"Fox\", \"Golf\", \"Golf Cabrio\", \"Golf Plus\", \"Golf Sportvan\", \"Golf Variant\", \"Jetta\", \"LT\", \"Lupo\", \"Multivan\", \"New Beetle\", \"New Beetle Cabrio\", \"Passat\", \"Passat Alltrack\", \"Passat CC\", \"Passat Variant\", \"Passat Variant Van\", \"Phaeton\", \"Polo\", \"Polo Van\", \"Polo Variant\", \"Scirocco\", \"Sharan\", \"T4\", \"T4 Caravelle\", \"T4 Multivan\", \"T5\", \"T5 Caravelle\", \"T5 Multivan\", \"T5 Transporter Shuttle\", \"Tiguan\", \"Touareg\", \"Touran\"]},\n" +
                "{\"brand\": \"Suzuki\", \"models\": [\"Alto\", \"Baleno\", \"Baleno kombi\", \"Grand Vitara\", \"Grand Vitara XL-7\", \"Ignis\", \"Jimny\", \"Kizashi\", \"Liana\", \"Samurai\", \"Splash\", \"Swift\", \"SX4\", \"SX4 Sedan\", \"Vitara\", \"Wagon R+\"]},\n" +
                "{\"brand\": \"Mercedes-Benz\", \"models\": [\"100 D\", \"115\", \"124\", \"126\", \"190\", \"190 D\", \"190 E\", \"200 - 300\", \"200 D\", \"200 E\", \"210 Van\", \"210 kombi\", \"310 Van\", \"310 kombi\", \"230 - 300 CE Coupé\", \"260 - 560 SE\", \"260 - 560 SEL\", \"500 - 600 SEC Coupé\", \"Trieda A\", \"A\", \"A L\", \"AMG GT\", \"Trieda B\", \"Trieda C\", \"C\", \"C Sportcoupé\", \"C T\", \"Citan\", \"CL\", \"CL\", \"CLA\", \"CLC\", \"CLK Cabrio\", \"CLK Coupé\", \"CLS\", \"Trieda E\", \"E\", \"E Cabrio\", \"E Coupé\", \"E T\", \"Trieda G\", \"G Cabrio\", \"GL\", \"GLA\", \"GLC\", \"GLE\", \"GLK\", \"Trieda M\", \"MB 100\", \"Trieda R\", \"Trieda S\", \"S\", \"S Coupé\", \"SL\", \"SLC\", \"SLK\", \"SLR\", \"Sprinter\"]},\n" +
                "{\"brand\": \"Saab\", \"models\": [\"9-3\", \"9-3 Cabriolet\", \"9-3 Coupé\", \"9-3 SportCombi\", \"9-5\", \"9-5 SportCombi\", \"900\", \"900 C\", \"900 C Turbo\", \"9000\"]},\n" +
                "{\"brand\": \"Audi\", \"models\": [\"100\", \"100 Avant\", \"80\", \"80 Avant\", \"80 Cabrio\", \"90\", \"A1\", \"A2\", \"A3\", \"A3 Cabriolet\", \"A3 Limuzina\", \"A3 Sportback\", \"A4\", \"A4 Allroad\", \"A4 Avant\", \"A4 Cabriolet\", \"A5\", \"A5 Cabriolet\", \"A5 Sportback\", \"A6\", \"A6 Allroad\", \"A6 Avant\", \"A7\", \"A8\", \"A8 Long\", \"Q3\", \"Q5\", \"Q7\", \"R8\", \"RS4 Cabriolet\", \"RS4/RS4 Avant\", \"RS5\", \"RS6 Avant\", \"RS7\", \"S3/S3 Sportback\", \"S4 Cabriolet\", \"S4/S4 Avant\", \"S5/S5 Cabriolet\", \"S6/RS6\", \"S7\", \"S8\", \"SQ5\", \"TT Coupé\", \"TT Roadster\", \"TTS\"]},\n" +
                "{\"brand\": \"Kia\", \"models\": [\"Avella\", \"Besta\", \"Carens\", \"Carnival\", \"Cee`d\", \"Cee`d SW\", \"Cerato\", \"K 2500\", \"Magentis\", \"Opirus\", \"Optima\", \"Picanto\", \"Pregio\", \"Pride\", \"Pro Cee`d\", \"Rio\", \"Rio Combi\", \"Rio sedan\", \"Sephia\", \"Shuma\", \"Sorento\", \"Soul\", \"Sportage\", \"Venga\"]},\n" +
                "{\"brand\": \"Land Rover\", \"models\": [\"109\", \"Defender\", \"Discovery\", \"Discovery Sport\", \"Freelander\", \"Range Rover\", \"Range Rover Evoque\", \"Range Rover Sport\"]},\n" +
                "{\"brand\": \"Dodge\", \"models\": [\"Avenger\", \"Caliber\", \"Challenger\", \"Charger\", \"Grand Caravan\", \"Journey\", \"Magnum\", \"Nitro\", \"RAM\", \"Stealth\", \"Viper\"]},\n" +
                "{\"brand\": \"Chrysler\", \"models\": [\"300 C\", \"300 C Touring\", \"300 M\", \"Crossfire\", \"Grand Voyager\", \"LHS\", \"Neon\", \"Pacifica\", \"Plymouth\", \"PT Cruiser\", \"Sebring\", \"Sebring Convertible\", \"Stratus\", \"Stratus Cabrio\", \"Town & Country\", \"Voyager\"]},\n" +
                "{\"brand\": \"Ford\", \"models\": [\"Aerostar\", \"B-Max\", \"C-Max\", \"Cortina\", \"Cougar\", \"Edge\", \"Escort\", \"Escort Cabrio\", \"Escort kombi\", \"Explorer\", \"F-150\", \"F-250\", \"Fiesta\", \"Focus\", \"Focus C-Max\", \"Focus CC\", \"Focus kombi\", \"Fusion\", \"Galaxy\", \"Grand C-Max\", \"Ka\", \"Kuga\", \"Maverick\", \"Mondeo\", \"Mondeo Combi\", \"Mustang\", \"Orion\", \"Puma\", \"Ranger\", \"S-Max\", \"Sierra\", \"Street Ka\", \"Tourneo Connect\", \"Tourneo Custom\", \"Transit\", \"Transit\", \"Transit Bus\", \"Transit Connect LWB\", \"Transit Courier\", \"Transit Custom\", \"Transit kombi\", \"Transit Tourneo\", \"Transit Valnik\", \"Transit Van\", \"Transit Van 350\", \"Windstar\"]},\n" +
                "{\"brand\": \"Hummer\", \"models\": [\"H2\", \"H3\"]},\n" +
                "{\"brand\": \"Hyundai\", \"models\": [\"Accent\", \"Atos\", \"Atos Prime\", \"Coupé\", \"Elantra\", \"Galloper\", \"Genesis\", \"Getz\", \"Grandeur\", \"H 350\", \"H1\", \"H1 Bus\", \"H1 Van\", \"H200\", \"i10\", \"i20\", \"i30\", \"i30 CW\", \"i40\", \"i40 CW\", \"ix20\", \"ix35\", \"ix55\", \"Lantra\", \"Matrix\", \"Santa Fe\", \"Sonata\", \"Terracan\", \"Trajet\", \"Tucson\", \"Veloster\"]},\n" +
                "{\"brand\": \"Infiniti\", \"models\": [\"EX\", \"FX\", \"G\", \"G Coupé\", \"M\", \"Q\", \"QX\"]},\n" +
                "{\"brand\": \"Jaguar\", \"models\": [\"Daimler\", \"F-Pace\", \"F-Type\", \"S-Type\", \"Sovereign\", \"X-Type\", \"X-type Estate\", \"XE\", \"XF\", \"XJ\", \"XJ12\", \"XJ6\", \"XJ8\", \"XJ8\", \"XJR\", \"XK\", \"XK8 Convertible\", \"XKR\", \"XKR Convertible\"]},\n" +
                "{\"brand\": \"Jeep\", \"models\": [\"Cherokee\", \"Commander\", \"Compass\", \"Grand Cherokee\", \"Patriot\", \"Renegade\", \"Wrangler\"]},\n" +
                "{\"brand\": \"Nissan\", \"models\": [\"100 NX\", \"200 SX\", \"350 Z\", \"350 Z Roadster\", \"370 Z\", \"Almera\", \"Almera Tino\", \"Cabstar E - T\", \"Cabstar TL2 Valnik\", \"e-NV200\", \"GT-R\", \"Insterstar\", \"Juke\", \"King Cab\", \"Leaf\", \"Maxima\", \"Maxima QX\", \"Micra\", \"Murano\", \"Navara\", \"Note\", \"NP300 Pickup\", \"NV200\", \"NV400\", \"Pathfinder\", \"Patrol\", \"Patrol GR\", \"Pickup\", \"Pixo\", \"Primastar\", \"Primastar Combi\", \"Primera\", \"Primera Combi\", \"Pulsar\", \"Qashqai\", \"Serena\", \"Sunny\", \"Terrano\", \"Tiida\", \"Trade\", \"Vanette Cargo\", \"X-Trail\"]},\n" +
                "{\"brand\": \"Volvo\", \"models\": [\"240\", \"340\", \"360\", \"460\", \"850\", \"850 kombi\", \"C30\", \"C70\", \"C70 Cabrio\", \"C70 Coupé\", \"S40\", \"S60\", \"S70\", \"S80\", \"S90\", \"V40\", \"V50\", \"V60\", \"V70\", \"V90\", \"XC60\", \"XC70\", \"XC90\"]},\n" +
                "{\"brand\": \"Daewoo\", \"models\": [\"Espero\", \"Kalos\", \"Lacetti\", \"Lanos\", \"Leganza\", \"Lublin\", \"Matiz\", \"Nexia\", \"Nubira\", \"Nubira kombi\", \"Racer\", \"Tacuma\", \"Tico\"]},\n" +
                "{\"brand\": \"Fiat\", \"models\": [\"1100\", \"126\", \"500\", \"500L\", \"500X\", \"850\", \"Barchetta\", \"Brava\", \"Cinquecento\", \"Coupé\", \"Croma\", \"Doblo\", \"Doblo Cargo\", \"Doblo Cargo Combi\", \"Ducato\", \"Ducato Van\", \"Ducato Kombi\", \"Ducato Podvozok\", \"Florino\", \"Florino Combi\", \"Freemont\", \"Grande Punto\", \"Idea\", \"Linea\", \"Marea\", \"Marea Weekend\", \"Multipla\", \"Palio Weekend\", \"Panda\", \"Panda Van\", \"Punto\", \"Punto Cabriolet\", \"Punto Evo\", \"Punto Van\", \"Qubo\", \"Scudo\", \"Scudo Van\", \"Scudo Kombi\", \"Sedici\", \"Seicento\", \"Stilo\", \"Stilo Multiwagon\", \"Strada\", \"Talento\", \"Tipo\", \"Ulysse\", \"Uno\", \"X1/9\"]},\n" +
                "{\"brand\": \"MINI\", \"models\": [\"Cooper\", \"Cooper Cabrio\", \"Cooper Clubman\", \"Cooper D\", \"Cooper D Clubman\", \"Cooper S\", \"Cooper S Cabrio\", \"Cooper S Clubman\", \"Countryman\", \"Mini One\", \"One D\"]},\n" +
                "{\"brand\": \"Rover\", \"models\": [\"200\", \"214\", \"218\", \"25\", \"400\", \"414\", \"416\", \"620\", \"75\"]},\n" +
                "{\"brand\": \"Smart\", \"models\": [\"Cabrio\", \"City-Coupé\", \"Compact Pulse\", \"Forfour\", \"Fortwo cabrio\", \"Fortwo coupé\", \"Roadster\"]}]\n";


        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<CarType>>(){}.getType();
        Collection<CarType> enums = gson.fromJson(json, collectionType);

//                    CaptainClientTransfer gsonObj = gson.fromJson(jsonArray.getJSONObject().toString(), CaptainClientTransfer.class);
        CarsTypes.clear();
        // captainClientTransfers.addAll(enums.getOrderList());
        CarsTypes= (List<CarType>) enums;



        for(int i=0;i<CarsTypes.size();i++){
            carTypeList.add(CarsTypes.get(i).getCarType());
            carMList=clearS;
        }

        carTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //    carMList=(CarsTypes.get(position).getCarModel());
                if(position==0){
                    carMList=clearS;
                    carT="";
                }else {
                    carMList=(CarsTypes.get(position-1).getCarModel());
                    carT = carTypeList.get(position);
                }
                fillSpinnerAdapter2();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        carModelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                carM=carMList.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(carTypeList.size()!=0) {
            carTypeList.add(0,"");
            carT ="";
        }

        if(carModelList.size()!=0) {
            carModelList.addAll(Collections.singleton(clearS));
            carM = "";
        }
        fillSpinnerAdapter();
        fillSpinnerAdapter2();
    }

    void fillSpinnerAdapter() {

        carTAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, carTypeList);
        carTAdapter.setDropDownViewResource(R.layout.spinner_drop_down_layout);
        carTypeSpinner.setAdapter(carTAdapter);
        //carTypeSpinner.setOnItemSelectedListener(this);

    }

    void fillSpinnerAdapter2() {

        carMAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, carMList);
        carMAdapter.setDropDownViewResource(R.layout.spinner_drop_down_layout);
        carModelSpinner.setAdapter(carMAdapter);
        //carModelSpinner.setOnItemSelectedListener(this);

    }


    private void initialization() {
        clearS=new ArrayList<>();
        clearS.add("");
        carTypeList=new ArrayList<>();
        carModelList=new ArrayList<>();
        carMList=new ArrayList<>();
        CarsTypes=new ArrayList<>();
        next = findViewById(R.id.btn_next);
        singUp = findViewById(R.id.singUp);
        userName = findViewById(R.id.userName);
        phoneNo = findViewById(R.id.phoneNo);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
      //  carType = findViewById(R.id.carType);
      //  carModel = findViewById(R.id.carModel);
        carLot = findViewById(R.id.carLot);
        carColor = findViewById(R.id.carColor);
        imagwPro = findViewById(R.id.imagwPro);
        sing_up_user_info = findViewById(R.id.sing_up_user_info);
        sing_up_car_info = findViewById(R.id.sing_up_car_info);
        carTypeSpinner=findViewById(R.id.carTypeSpinner);
        carModelSpinner=findViewById(R.id.carModelSpinner);
        colorList=findViewById(R.id.colorList);
        imageG=findViewById(R.id.imageG);
        colorList.setVisibility(View.GONE);

        carColorList=new ArrayList<>();

        carColorList.add(new ColorCarModel("Red","#FF0000"));
        carColorList.add(new ColorCarModel("Black","#000000"));
        carColorList.add(new ColorCarModel("Blue","#0000FF"));
        carColorList.add(new ColorCarModel("White","#FFFFFF"));
        carColorList.add(new ColorCarModel("Gray","#808080"));
        carColorList.add(new ColorCarModel("Green","#008000"));
        carColorList.add(new ColorCarModel("Silver","#C0C0C0"));
        carColorList.add(new ColorCarModel("Brown","#8B4513"));
        carColorList.add(new ColorCarModel("Beige","#F5F5DC"));
        carColorList.add(new ColorCarModel("Orange","#FFA500"));
        carColorList.add(new ColorCarModel("Gold","#FFD700"));
        carColorList.add(new ColorCarModel("Yellow","#FFFF00"));
        carColorList.add(new ColorCarModel("Purple","#800080"));


        next.setOnClickListener(this);
        singUp.setOnClickListener(this);
        carYear =findViewById(R.id.carYear);
        myCalendar = Calendar.getInstance();
        myFormat = "yyyy";
        sdf = new SimpleDateFormat(myFormat, Locale.US);

        carYear.setText(sdf.format(myCalendar.getTime()));

        imageD =  findViewById(R.id.imageD);
        imageC=findViewById(R.id.imageC);

        imageC.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
        imageD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //https://www.google.com/#q=
              //  Toast.makeText(SingUpValetActivityApp.this, imageD.getText().toString(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/images?q=" + carT+" "+carM+" "+carYear.getText().toString()+" "+carColor.getText().toString()+" png")));
            }
        });
        imageG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openGallery();

            }
        });

        carColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SimpleColorWheelDialog.build()
//                        .color(0xFFCF4747) // optional initial color
//                        .alpha(true)
//                        .show(SingUpValetActivityApp.this,COLOR_PICKER);

                colorList.setVisibility(View.VISIBLE);
                listAdapterColor = new ListAdapterColor(SingUpValetActivityApp.this, carColorList);
                colorList.setAdapter(listAdapterColor);


            }
        });

        carYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  createDialogWithoutDateField().show();
                new DatePickerDialog(SingUpValetActivityApp.this, openDatePickerDialog(1), myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    @Override
    public boolean onResult(@NonNull String dialogTag, int which, @NonNull Bundle extras) {
        if (COLOR_PICKER.equals(dialogTag) && which == BUTTON_POSITIVE){
            int color = extras.getInt(SimpleColorWheelDialog.COLOR);
           // Toast.makeText(this, "Color = "+color, Toast.LENGTH_SHORT).show();
            carColor.setText(""+color);
            carColor.setBackgroundColor(color);
            return true;
        }
        return false;
    }

    public DatePickerDialog.OnDateSetListener openDatePickerDialog(final int flag) {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    carYear.setText(sdf.format(myCalendar.getTime()));


            }

        };
        return date;
    }



    private DatePickerDialog createDialogWithoutDateField() {
        DatePickerDialog dpd = new DatePickerDialog(this, null, 2021, 1, 24);
        try {
            java.lang.reflect.Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
            for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField.get(dpd);
                    java.lang.reflect.Field[] datePickerFields = datePickerDialogField.getType().getDeclaredFields();
                    for (java.lang.reflect.Field datePickerField : datePickerFields) {
                        Log.i("test", datePickerField.getName());
                        if ("mDaySpinner".equals(datePickerField.getName())) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }
            }



        }
        catch (Exception ex) {
        }
        return dpd;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_next:

                checkNext();

                break;
            case R.id.singUp:

                saveClient();
                break;


        }
    }


    private void checkNext() {
//        if (!TextUtils.isEmpty(carLot.getText().toString())) {
            if (!TextUtils.isEmpty(userName.getText().toString())) {
                if (!TextUtils.isEmpty(password.getText().toString())) {
                    if (!TextUtils.isEmpty(email.getText().toString())) {
                        if (!TextUtils.isEmpty(phoneNo.getText().toString())) {
                            sing_up_user_info.setVisibility(View.GONE);
                            sing_up_car_info.setVisibility(View.VISIBLE);
                        } else {
                            phoneNo.setError("Required ");
                        }
                    } else {
                        email.setError("Required ");
                    }
                } else {
                    password.setError("Required ");
                }
            } else {
                userName.setError("Required ");
            }
//        } else {
//            carLot.setError("Required ");
//        }

    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    private void saveClient() {
        if (!TextUtils.isEmpty(carLot.getText().toString())) {
            if (!TextUtils.isEmpty(carT)) {
                if (!TextUtils.isEmpty(carM)) {
                    if (!TextUtils.isEmpty(carColor.getText().toString())) {
//

                        SingUpClientModel singUpClientModel = new SingUpClientModel();
                        singUpClientModel.setUserName(userName.getText().toString());
                        singUpClientModel.setPhoneNo(phoneNo.getText().toString());
                        singUpClientModel.setEmail(email.getText().toString());
                        singUpClientModel.setPassword(password.getText().toString());
                        singUpClientModel.setCarType(carT);
                        singUpClientModel.setCarColor(carColor.getText().toString());
                        singUpClientModel.setCarLot(carLot.getText().toString());
                        singUpClientModel.setCarModel(carM);
                        singUpClientModel.setCarPic(bitMapToString(YourPicBitmap1));

                        ExportJson exportJson = new ExportJson(SingUpValetActivityApp.this);
                    exportJson.SingUpCaptain_2(SingUpValetActivityApp.this, singUpClientModel);
//                        exportJson.SingUpCaptain(SingUpValetActivityApp.this, singUpClientModel.getSingUpJson(), singUpClientModel);

//                       exportJson.SendImage(SingUpValetActivityApp.this,bitMapToString(YourPicBitmap1));

//
                    } else {
                        carColor.setError("Required ");
                    }
                } else {
                    Toast.makeText(this, "Car Model Required ", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Car Type Required ", Toast.LENGTH_SHORT).show();
            }
        } else {
            carLot.setError("Required ");
        }
    }


    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_IMAGE);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 3 &&resultCode == Activity.RESULT_OK) {
            if (data != null) {
                fileUri = data.getData(); //added this line
                try {

                    YourPicBitmap1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileUri);

                    imagwPro.setImageBitmap(YourPicBitmap1);
                    imagwPro.setBackground(null);


                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
        }else  if (requestCode == 1888 && resultCode == Activity.RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                //thumbnail = getResizedBitmap(thumbnail, 100, 100);
                File picture = null;

                if (extras != null) {
//                Bitmap pic = extras.getParcelable("data");
                    YourPicBitmap1=thumbnail;
                    imagwPro.setImageBitmap(YourPicBitmap1);
                    imagwPro.setBackground(null);
                }
        }

    }


    public String bitMapToString(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] arr = baos.toByteArray();
            String result = Base64.encodeToString(arr, Base64.DEFAULT);
            return result;
        }

        return "";
    }

    public  void ColorInText(String colorName,String colorsNo){
        colorList.setVisibility(View.GONE);
        carColor.setBackgroundColor(Color.parseColor(colorsNo));
        carColor.setText(colorName);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 1888);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(SingUpValetActivityApp.this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 1888);
            } else {
                Toast.makeText(SingUpValetActivityApp.this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

}
