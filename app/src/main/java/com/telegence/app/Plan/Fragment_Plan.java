package com.telegence.app.Plan;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.telegence.app.R;
import com.telegence.app.Scheme_Get_Set;
import com.telegence.app.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.telegence.app.SimpleClasses.ApiRequest;
import com.telegence.app.SimpleClasses.Callback;
import com.telegence.app.SimpleClasses.Functions;
import com.telegence.app.SimpleClasses.Variables;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.downloader.request.DownloadRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Plan extends RootFragment {

    View view;
    Context context;
    ProgressBar pbar;
    RelativeLayout no_data_layout;
    ImageView image;
    TextView txtname,txtoffer,txtshortdesc,txtdescheading,txtdesc;


    public Fragment_Plan() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.frag_plan, container, false);
        context=getContext();
        no_data_layout = view.findViewById(R.id.no_data_layout);
        view.findViewById(R.id.download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Functions.check_permissions(getActivity())){
                    String download_url  = Variables.main_domain + "BHIMIUNI_GOLD_PLAN.pdf";
                    if(download_url!=null){

                        Functions.Show_determinent_loader(context,false,false);
                        PRDownloader.initialize(getActivity().getApplicationContext());
                        DownloadRequest prDownloader= PRDownloader.download(download_url, Functions.getDownloadsFolder(context), "BHIMIUNI_GOLD_PLAN.pdf")
                                .build()
                                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                                    @Override
                                    public void onStartOrResume() {

                                    }
                                })
                                .setOnPauseListener(new OnPauseListener() {
                                    @Override
                                    public void onPause() {

                                    }
                                })
                                .setOnCancelListener(new OnCancelListener() {
                                    @Override
                                    public void onCancel() {

                                    }
                                })
                                .setOnProgressListener(new OnProgressListener() {
                                    @Override
                                    public void onProgress(Progress progress) {

                                        int prog=(int)((progress.currentBytes*100)/progress.totalBytes);
                                        Functions.Show_loading_progress(prog);

                                    }
                                });


                        prDownloader.start(new OnDownloadListener() {
                            @Override
                            public void onDownloadComplete() {
                                Functions.cancel_determinent_loader();
                                Toast.makeText(getApplicationContext(), "Downloaded", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onError(Error error) {
                                //Toast.makeText(context, "Error"  + error , Toast.LENGTH_SHORT).show();
                                Toast.makeText(context, "Please try again.", Toast.LENGTH_SHORT).show();
                                Functions.cancel_determinent_loader();
                            }


                        });

                    }if(download_url!=null){

                        Functions.Show_determinent_loader(context,false,false);
                        PRDownloader.initialize(getActivity().getApplicationContext());
                        DownloadRequest prDownloader= PRDownloader.download(download_url, Functions.getDownloadsFolder(context), "BHIMIUNI_GOLD_PLAN.pdf")
                                .build()
                                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                                    @Override
                                    public void onStartOrResume() {

                                    }
                                })
                                .setOnPauseListener(new OnPauseListener() {
                                    @Override
                                    public void onPause() {

                                    }
                                })
                                .setOnCancelListener(new OnCancelListener() {
                                    @Override
                                    public void onCancel() {

                                    }
                                })
                                .setOnProgressListener(new OnProgressListener() {
                                    @Override
                                    public void onProgress(Progress progress) {

                                        int prog=(int)((progress.currentBytes*100)/progress.totalBytes);
                                        Functions.Show_loading_progress(prog);

                                    }
                                });


                        prDownloader.start(new OnDownloadListener() {
                            @Override
                            public void onDownloadComplete() {
                                Functions.cancel_determinent_loader();
                                Toast.makeText(getApplicationContext(), "Downloaded", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onError(Error error) {
                                //Toast.makeText(context, "Error"  + error , Toast.LENGTH_SHORT).show();
                                Toast.makeText(context, "Please try again.", Toast.LENGTH_SHORT).show();
                                Functions.cancel_determinent_loader();
                            }


                        });

                    }
                }
            }
        });
        image=view.findViewById(R.id.image);
        txtname=view.findViewById(R.id.txtname);
        txtoffer=view.findViewById(R.id.txtoffer);
        txtshortdesc=view.findViewById(R.id.txtshortdesc);
        txtdescheading=view.findViewById(R.id.txtdescheading);
        txtdesc=view.findViewById(R.id.txtdesc);
        pbar=view.findViewById(R.id.pbar);
        Call_Api_For_get_Plan_value();
        return view;
    }

    // Bottom two function will call the api and get all the videos form api and parse the json data
    private void Call_Api_For_get_Plan_value() {
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id",Variables.user_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Variables.get_plan, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Parse_plan_data(resp);
            }
        });


    }

    public void Parse_plan_data(String responce){

        try {
            JSONObject jsonObject=new JSONObject(responce);
            String code=jsonObject.optString("code");
            if(code.equals("200")){
                JSONArray bannerArray=jsonObject.getJSONArray("msg");
                pbar.setVisibility(View.GONE);
                if(bannerArray.length()==0){
                    no_data_layout.setVisibility(View.VISIBLE);
                }else{
                    JSONObject data = bannerArray.optJSONObject(0); //Top
                    txtname.setText(data.optString("banner_name"));
                    txtoffer.setText(data.optString("banner_offer_text"));
                    txtshortdesc.setText(data.optString("banner_offer_short_desc"));
                    txtdescheading.setText(data.optString("banner_desc_heading"));
                    txtdesc.setText(data.optString("banner_desc"));
                    String res= data.optString("banner_image");;
                    if(res!=null && !res.equalsIgnoreCase(""))
                        Picasso.get().load(res).placeholder(R.color.gray).into(image);

                    no_data_layout.setVisibility(View.GONE);
                }


            } else {
                Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                no_data_layout.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void OpenProfile(final Scheme_Get_Set item) {
        /*Profile_F profile_f = new Profile_F(new Fragment_Callback() {
            @Override
            public void Responce(Bundle bundle) {

            }
        });

        View view=getActivity().findViewById(R.id.MainMenuFragment);
        if(view!=null){
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
            Bundle args = new Bundle();
            args.putString("user_id",item.fb_id);
            args.putString("user_name",item.first_name+" "+item.last_name);
            args.putString("user_pic",item.profile_pic);
            profile_f.setArguments(args);
            transaction.addToBackStack(null);
            transaction.replace(R.id.MainMenuFragment, profile_f).commit();
        }
        else {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
            Bundle args = new Bundle();
            args.putString("user_id",item.fb_id);
            args.putString("user_name",item.first_name+" "+item.last_name);
            args.putString("user_pic",item.profile_pic);
            profile_f.setArguments(args);
            transaction.addToBackStack(null);
            transaction.replace(R.id.following_layout, profile_f).commit();
        }*/


    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


}
