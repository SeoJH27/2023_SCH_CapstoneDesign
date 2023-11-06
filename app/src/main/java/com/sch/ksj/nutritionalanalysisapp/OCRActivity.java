package com.sch.ksj.nutritionalanalysisapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;

public class OCRActivity extends MainActivity{
    ImageButton home;
    TextView text, con_text, result;
    Button Resultbutton;
    StringBuilder translateText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        ThreadClass thc = new ThreadClass();
        thc.execute();

        // 홈버튼
        home = (ImageButton) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OCRActivity.this, MainActivity.class));
            }
        });

        // 결과 저장 버튼
        Resultbutton = (Button) findViewById(R.id.Resultbutton);
        Resultbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //데이터베이스에 저장
                startActivity(new Intent(OCRActivity.this, MainActivity.class));
            }
        });

    }


    public class ThreadClass extends AsyncTask<String, String, String> {

        @Override
        public String doInBackground(String... strings) {
            Log.e("OCR Start", "1");
            return OcrProc.main();
        }

        @Override
        protected void onPostExecute(String result) {
            ReturnThreadResult(result);
        }
    }

    public void ReturnThreadResult(String result) {
        System.out.println("###  Return Thread Result");
        translateText = new StringBuilder();
        Log.i("text", result);

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("images");

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONArray jsonArray_fields = jsonArray.getJSONObject(i).getJSONArray("fields");

                for (int j = 0; j < jsonArray_fields.length(); j++) {

                    String inferText = jsonArray_fields.getJSONObject(j).getString("inferText");
                    translateText.append(inferText);
                    translateText.append(" ");
                }
            }
            Log.v("translateText", translateText.toString());
            Analyze(translateText);

        } catch (Exception e) {
            Log.e("translateText Error!", e.toString() + translateText.toString());
        }
    }

    public void Analyze(StringBuilder text) {
        String[] nut = {"탄수화물", "당류", "단백질", "지방", "트랜스지방", "포화지방", "콜레스테롤", "나트륨", "과당", "비타민A", "비타민C", "칼슘", "철분", "마그네슘"};
        String textA = text.toString();
        int[] nut_int = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        String[] nut_measure = {"g", "g", "g", "g", "g", "g", "mg", "mg", "g", "㎍", "mg", "mg", "mg", "mg"};

        //받아온 텍스트 조사
        if (text.length() > 0){
            // index: 숫자 시작 위치
            // num_integer: 숫자값 정수로 저장
            int index, nut_integer;
            String nut_m;

            //영양분 하나씩 조사
            for(int i = 0; i < nut.length; i++){
                nut_integer = 0;
                // 해당 영양분이 존재하면
                if(text.indexOf(nut[i]) != -1){
                    // nint: 숫자값 문자열로 저장
                    String nint = "";
                    //영양분 이름의 시작 인덱스 지점 + 영양분 이름 길이
                    index = text.indexOf(nut[i]) + nut[i].length();

                    // 빈칸 건너 뛰고 숫자 찾기
                    for(int j = index; j < textA.length(); j++){
                        char c = textA.charAt(j);

                        // 해당 위치의 문자가 숫자면 값을 저장
                        if(isInteger(Character.toString(c))){
                            nint += c;
                        }
                        else if(c == ' '){
                            //빈칸 넘김
                        }
                        // 아닐시 루프 멈춤
                        else {
                            Log.v("int", nint);
                            if(nint != null && isInteger(nint)) {
                                nut_integer = Integer.valueOf(nint);
                            }
                            break;
                        }
                    }

                    // 여태 저장한 영양분의 숫자값을 문자로 저장
                    //nut_integer = Integer.parseInt(nint.trim());
                    nut_int[i] = nut_integer;
                }

                //해당 영양분이 존재하지 않으면 넘어감
            }

            AnalyzeResult(nut, nut_int, nut_measure);
        }
        else
            Log.e("Analysis Error", "text is empty");
    }

    public void AnalyzeResult(String[] nut, int[] nut_int, String[] nut_messure){
        text = (TextView) findViewById(R.id.text);
        con_text = (TextView) findViewById(R.id.con_text);
        result = (TextView) findViewById(R.id.result);
        String c = "", t = "", r = "";

        try {
            for (int i = 0; i < nut.length; i++) {
                c += nut[i] + ": \n";
                if (nut_messure[i] == null)
                    t += nut_int[i] + "\n";
                else
                    t += nut_int[i] + nut_messure[i] + "\n";

            }

            con_text.setText(c);
            text.setText(t);
        } catch (Exception e){
            Log.e("text Error", e.getMessage());
        }

        try {
            for (int i = 0; i < nut.length; i++) {
                switch (nut[i]) {
                    case "당류":
                        if (nut_int[i] >= 100)
                            r += "당류의 하루 권장량은 100g으로\n권장량 초과시 질병으로는, 충치, 비만, 심장병, 고혈압, 제2형 당뇨병 등의 발생 위험이 있습니다.\n";
                        else if (nut_int[i] > 0)
                            r += "당류의 하루 권장량은 100g이므로 안전합니다!\n";
                        break;
                    case "단백질":
                        if (nut_int[i] >= 55)
                            r += "단백질의 하루 권장량은 사람의 체중마다 다르지만 평균적인 권장량은 55g으로\n 권장량 초과시 통풍,콩팥 질환(신장),변비등이 발생 위험이 있습니다.\n";
                        else if (nut_int[i] > 0)
                            r += "단백질의 하루 권장량은 55g이므로 안전합니다!\n";
                        break;
                    case "지방":
                        if ((nut_int[i] >= 53 && info.getInstance().getGender() == true) || (nut_int[i] >= 52 && info.getInstance().getGender() == false))
                            r += "지방의 하루 권장량은 성인 남성 기준 53g, 성인 여성 기준 42g정도이며, 권장량 초과시 각종 성인병과 대장암 발병률이 올라갑니다.\n";
                        else if (nut_int[i] > 0)
                            r += "지방의 하루 권장량은 성인 남성 기준 53g, 성인 여성 기준 42g정도이므로 안전합니다!\n";
                        break;
                    case "트랜스지방":
                        if (nut_int[i] >= 2)
                            r += "트랜스지방의 1일 섭취량의 열량의 1%이하로 제한하고 있으며, 이에 따라 하루에 2g 이하 입니다.\n과다 복용시 혈중 콜레스테롤 수치를 높이고 동맥 경화,심장 질환, 고혈압 발생 위험이 있습니다.\n";
                        else if (nut_int[i] > 0)
                            r += "트랜스지방의 1일 섭취량의 열량의 1%이하로 제한하고 있으며, 이에 따라 하루에 2g 이하이므로 안전합니다!\n";
                        break;
                    case "포화지방":
                        if ((nut_int[i] >= 30 && info.getInstance().getGender() == true) || (nut_int[i] >= 25 && info.getInstance().getGender() == false))
                            r += "포화지방의 일일 권장량은 평균적으로 성인 남성의 경우 대략 20~30g, 성인 여성은 15~25g 정도가 권장됩니다.\n과다 복용을 하면 심장질환, 고혈압, 비만, 당뇨병 등의 위험이 상승합니다.\n";
                        else if (nut_int[i] > 0)
                            r += "포화지방의 일일 권장량은 평균적으로 성인 남성의 경우 대략 20~30g, 성인 여성은 15~25g 정도가 권장되므로 안전합니다!\n";
                        break;
                    case "콜레스테롤":
                        if (nut_int[i] >= 300)
                            r += "콜레스테롤의 하루 권장량은 300mg로,\n 권장량 초과시 혈관 막힘 증상과, 고혈압을 유발할 수 있습니다.\n";
                        else if (nut_int[i] > 0)
                            r += "콜레스테롤의 하루 권장량은 300mg으로 안전합니다!\n";
                        break;
                    case "나트륨":
                        if (nut_int[i] >= 2000)
                            r += "나트륨의 하루 권장량은 2000mg 정도로,\n권장량 초과시 부종, 두통, 관절염, 골다공증, 위염 등의 부작용이 생깁니다.\n";
                        else if (nut_int[i] > 0)
                            r += "나트륨의 하루 권장량은 2000mg 정도이므로 안전합니다!\n";
                        break;
                    case "과당":
                        if (nut_int[i] >= 50)
                            r += "과당의 하루 권장량은 평균적으로 50g으로 기준으로 합니다.\n과다 섭취시 피로를 쉽게 느끼며, 몸이 잘 붓고, 피부가 나빠지는 등 건강에 악 영향을 끼칠수 있습니다.\n";
                        else if (nut_int[i] > 0)
                            r += "과당의 하루 권장량은 평균적으로 50g으로 기준이으로 안전합니다!\n";
                        break;
                    case "비타민A":
                        if (nut_int[i] >= 800)
                            r += "비타민A의 1일 권장 섭취량은 약 700 - 800㎍ 정도로,\n권장량 초과시 두통, 피부 건조 및 가려움, 간장 비대 등이 나타 납니다.\n특히 임산기에 과다 섭취시 사산, 출생 기형 등이 발생할 수 있습니다.\n";
                        else if (nut_int[i] > 0)
                            r += "비타민A의 1일 권장 섭취량은 약 700 - 800㎍ 정도로 안전합니다!\n";
                        break;
                    case "비타민C":
                        if (nut_int[i] >= 100)
                            r += "비타민C의 하루 권장량으로 성인은 100 mg이고 임산부와 흡연자는 130-140 mg 정도이며.\n하루 최대 상한 섭취량은 2000mg이다. 권장량 초과시 설사, 속쓰림, 복통, 신장 결석과 같은 증상이 발생할 수 있습니다.\n";
                        else if (nut_int[i] > 0)
                            r += "비타민C의 하루 권장량으로 성인은 100 mg이고 임산부와 흡연자는 130-140 mg 정도이며.\n하루 최대 상한 섭취량은 2000mg이므로 안전합니다!\n";
                        break;
                    case "칼슘":
                        if (nut_int[i] >= 2500)
                            r += "칼슘의 하루 권장량은 성인기준 2500mg입니다.\n권장량 초과시 혈중 칼슘 수치를 높일 수 있으며, 이것을 고칼슘 혈증이라 합니다. 신장 결석 및 신장 손상을 포함한 건강 문제를 일으킬 수 있습니다.\n";
                        else if (nut_int[i] > 0)
                            r += "칼슘의 하루 권장량은 성인기준 2500mg이므로 안전합니다!\n";
                        break;
                    case "철분":
                        if ((nut_int[i] >= 10 && info.getInstance().getGender() == true) || (nut_int[i] >= 14 && info.getInstance().getGender() == false))
                            r += "철분의 하루 권장량은 성인 남성의 기준 9~10mg이고, 성인 여성의 경우 8~14mg입니다.\n하루 권장량 초과시 배탈, 변비, 메스꺼움, 복통, 구토 및 설사가 발생할 수 있고, 심할 경우 위염 및 위궤양이 유발될 수 있습니다.\n극도로 많은 양을 섭취할 경우 여러 장기들이 제 기능을 하지 못하는 장기 부전, 혼수 상태, 경련 및 사망 할수도 있습니다.\n";
                        else if (nut_int[i] > 0)
                            r += "철분의 하루 권장량은 성인 남성의 기준 9~10mg이고, 성인 여성의 경우 8~14mg이므로 안전합니다!\n";
                        break;
                    case "마그네슘":
                        if (nut_int[i] >= 420)
                            r += "마그네슘의 하루 권장량은 320~420mg정도 입니다.\n하루 권장량 초과 시에는 신장 장애, 위장 장애, 소화 장애 등이 발생할 수 있다.\n";
                        else if (nut_int[i] > 0)
                            r += "마그네슘의 하루 권장량은 320~420mg정도이므로 안전합니다!\n";
                        break;
                }
                result.setText(r);
            }
        } catch (Exception e){
            Log.e("result Error", e.getMessage());
        }
    }

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
