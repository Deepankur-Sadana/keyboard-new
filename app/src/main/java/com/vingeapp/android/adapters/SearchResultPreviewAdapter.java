package com.vingeapp.android.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.vingeapp.android.R;
import com.vingeapp.android.interfaces.RecyclerViewClickInterface;
import com.vingeapp.android.library.LinkPreviewCallback;
import com.vingeapp.android.library.SourceContent;
import com.vingeapp.android.library.TextCrawler;
import com.vingeapp.android.serverGoogleSearchResponse.Link;

import java.util.ArrayList;

/**
 * Created by deepankursadana on 30/06/17.
 */

public class SearchResultPreviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<Link> linkArrayList;
    private final static int NO_RESULT_TYPE = 11, LINK_TYPE = 44;
    private RecyclerViewClickInterface clickInterface;


    public SearchResultPreviewAdapter(Context context, ArrayList<Link> linkArrayList,RecyclerViewClickInterface clickInterface) {
        this.context = context;
        this.linkArrayList = linkArrayList;
        this.clickInterface = clickInterface;
    }

    public void setLinkArrayList(ArrayList<Link> linkArrayList) {
        this.linkArrayList = linkArrayList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==LINK_TYPE)
        return new VHItem(new FrameLayout(parent.getContext()));
        else {
            TextView textView = new TextView(parent.getContext());
            textView.setText("Sorry, no results found");
            return new VHNoResults(textView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VHItem){
            Link link = linkArrayList.get(position);
            VHItem vhItem = (VHItem) holder;
            vhItem.titleTv.setText(link.getTitle());
            vhItem.descriptionTv.setText(link.getDescription());
            ((VHItem) holder).rootView.setTag(link);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return linkArrayList == null || linkArrayList.size() == 0 ? NO_RESULT_TYPE : LINK_TYPE;
    }


    @Override
    public int getItemCount() {
        return linkArrayList == null || linkArrayList.size() == 0 ? 1 : linkArrayList.size();

    }

    private class VHNoResults extends RecyclerView.ViewHolder {
        View rootView;

        VHNoResults(View v) {
            super(v);
            rootView = v;
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickInterface!=null)
                        clickInterface.onItemClick(RecyclerViewClickInterface.CLICK_TYPE_NORMAL, -1, view.getTag());
                }
            });

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    if (clickInterface != null)
                        clickInterface.onItemClick(RecyclerViewClickInterface.CLICK_TYPE_LONG_PRESS, -1, v.getTag());
                    return true;
                }
            });
        }
    }

    private class VHItem extends RecyclerView.ViewHolder {
        View rootView;
        TextView titleTv, descriptionTv;
        VHItem(View v) {
            super(v);
            rootView = v;
            ((ViewGroup) rootView).addView(getLayoutInflater().inflate(R.layout.card_link_preview_pre_load, null));
            descriptionTv = (TextView) rootView.findViewById(R.id.descriptionTV);
            titleTv = (TextView) rootView.findViewById(R.id.titleTV);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickInterface != null)
                        clickInterface.onItemClick(RecyclerViewClickInterface.CLICK_TYPE_NORMAL, -1, view.getTag());
                }
            });
        }
    }

    LinkPreviewCallback getCallback(){
    LinkPreviewCallback callback = new LinkPreviewCallback() {
        /**
         * This view is used to be updated or added in the layout after getting
         * the result
         */
        private View mainView;
        private LinearLayout linearLayout;
        private View loading;
        private ImageView imageView;
        private Bitmap[] currentImageSet;

        private Bitmap currentImage;
        private int currentItem = 0;
        private int countBigImages = 0;

        public void setLinearLayout(LinearLayout linearLayout) {
            this.linearLayout = linearLayout;
        }

        private String currentTitle, currentDescription, currentUrl, currentCannonicalUrl;

        @Override
        public void onPre() {

            currentImageSet = null;
            currentItem = 0;

//            postButton.setVisibility(View.GONE);

            currentImage = null;
            currentTitle = currentDescription = currentUrl = currentCannonicalUrl = "";


            /** Inflating the preview layout */
//            mainView = getLayoutInflater().inflate(R.layout.main_view, null);
//
//            linearLayout = (LinearLayout) mainView.findViewById(R.id.external);

            /**
             * Inflating a loading layout into Main View LinearLayout
             */
            loading = getLayoutInflater().inflate(R.layout.loading,
                    linearLayout);

//            dropPreview.addView(mainView);
        }

        @Override
        public void onPos(final SourceContent sourceContent, boolean isNull) {

            /** Removing the loading layout */
            linearLayout.removeAllViews();

            if (isNull || sourceContent.getFinalUrl().equals("")) {
                /**
                 * Inflating the content layout into Main View LinearLayout
                 */
                View failed = getLayoutInflater().inflate(R.layout.failed,
                        linearLayout);

                TextView titleTextView = (TextView) failed
                        .findViewById(R.id.text);
                titleTextView.setText(context.getString(R.string.failed_preview) + "\n"
                        + sourceContent.getFinalUrl());

                failed.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
//                        releasePreviewArea();
                    }
                });

            } else {

                currentImageSet = new Bitmap[sourceContent.getImages().size()];

                /**
                 * Inflating the content layout into Main View LinearLayout
                 */
                final View content = getLayoutInflater().inflate(
                        R.layout.card_url_preview, linearLayout);

                /** Fullfilling the content layout */
                final LinearLayout infoWrap = (LinearLayout) content
                        .findViewById(R.id.info_wrap);
                final LinearLayout titleWrap = (LinearLayout) infoWrap
                        .findViewById(R.id.title_wrap);
                final LinearLayout thumbnailOptions = (LinearLayout) content
                        .findViewById(R.id.thumbnail_options);

                final ImageView imageSet = (ImageView) content
                        .findViewById(R.id.image_post_set);


                final TextView titleTextView = (TextView) titleWrap
                        .findViewById(R.id.title);
                final TextView titleEditText = (TextView) titleWrap
                        .findViewById(R.id.input_title);
                final TextView urlTextView = (TextView) content
                        .findViewById(R.id.url);
                final TextView descriptionTextView = (TextView) content
                        .findViewById(R.id.description);
                final TextView descriptionEditText = (TextView) content
                        .findViewById(R.id.input_description);
                final TextView countTextView = (TextView) thumbnailOptions
                        .findViewById(R.id.count);
                final Button previousButton = (Button) thumbnailOptions
                        .findViewById(R.id.post_previous);
                final Button forwardButton = (Button) thumbnailOptions
                        .findViewById(R.id.post_forward);

//                editTextTitlePost = titleEditText;
//                editTextDescriptionPost = descriptionEditText;

                titleTextView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        titleTextView.setVisibility(View.GONE);

                        titleEditText.setText(TextCrawler
                                .extendedTrim(titleTextView.getText()
                                        .toString()));
                        titleEditText.setVisibility(View.VISIBLE);
                    }
                });
                titleEditText
                        .setOnEditorActionListener(new TextView.OnEditorActionListener() {

                            @Override
                            public boolean onEditorAction(TextView arg0,
                                                          int arg1, KeyEvent arg2) {

                                if (arg2.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                                    titleEditText.setVisibility(View.GONE);

                                    currentTitle = TextCrawler
                                            .extendedTrim(titleEditText
                                                    .getText().toString());

                                    titleTextView.setText(currentTitle);
                                    titleTextView.setVisibility(View.VISIBLE);

                                }

                                return false;
                            }
                        });
                descriptionTextView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        descriptionTextView.setVisibility(View.GONE);

                        descriptionEditText.setText(TextCrawler
                                .extendedTrim(descriptionTextView.getText()
                                        .toString()));
                        descriptionEditText.setVisibility(View.VISIBLE);
                    }
                });
                descriptionEditText
                        .setOnEditorActionListener(new TextView.OnEditorActionListener() {

                            @Override
                            public boolean onEditorAction(TextView arg0,
                                                          int arg1, KeyEvent arg2) {

                                if (arg2.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                                    descriptionEditText
                                            .setVisibility(View.GONE);

                                    currentDescription = TextCrawler
                                            .extendedTrim(descriptionEditText
                                                    .getText().toString());

                                    descriptionTextView
                                            .setText(currentDescription);
                                    descriptionTextView
                                            .setVisibility(View.VISIBLE);

                                }

                                return false;
                            }
                        });


                previousButton.setEnabled(false);
                previousButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        if (currentItem > 0)
                            changeImage(previousButton, forwardButton,
                                    currentItem - 1, sourceContent,
                                    countTextView, imageSet, sourceContent
                                            .getImages().get(currentItem - 1),
                                    currentItem, currentImageSet);
                    }
                });
                forwardButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        if (currentItem < sourceContent.getImages().size() - 1)
                            changeImage(previousButton, forwardButton,
                                    currentItem + 1, sourceContent,
                                    countTextView, imageSet, sourceContent
                                            .getImages().get(currentItem + 1),
                                    currentItem, currentImageSet);
                    }
                });

                if (sourceContent.getImages().size() > 0) {

                    if (sourceContent.getImages().size() > 1) {
                        countTextView.setText("1 " + context.getString(R.string.of)
                                + " " + sourceContent.getImages().size());

                        thumbnailOptions.setVisibility(View.VISIBLE);
                    }

                    UrlImageViewHelper.setUrlDrawable(imageSet, sourceContent
                            .getImages().get(0), new UrlImageViewCallback() {

                        @Override
                        public void onLoaded(ImageView imageView,
                                             Bitmap loadedBitmap, String url,
                                             boolean loadedFromCache) {
                            if (loadedBitmap != null) {
                                currentImage = loadedBitmap;
                                currentImageSet[0] = loadedBitmap;
                            }
                        }
                    });

                } else {
                    showHideImage(imageSet, infoWrap, false);
                }

                if (sourceContent.getTitle().equals(""))
                    sourceContent.setTitle(context.getString(R.string.enter_title));
                if (sourceContent.getDescription().equals(""))
                    sourceContent
                            .setDescription(context.getString(R.string.enter_description));

                titleTextView.setText(sourceContent.getTitle());
                urlTextView.setText(sourceContent.getCannonicalUrl());
                descriptionTextView.setText(sourceContent.getDescription());

            }

            currentTitle = sourceContent.getTitle();
            currentDescription = sourceContent.getDescription();
            currentUrl = sourceContent.getUrl();
            currentCannonicalUrl = sourceContent.getCannonicalUrl();
        }
    };
        return callback;

}
    /**
     * Change the current image in image set
     */
    private void changeImage(Button previousButton, Button forwardButton,
                             final int index, SourceContent sourceContent,
                             TextView countTextView, ImageView imageSet, String url,
                             final int current, final Bitmap[] currentImageSet) {

//        if (currentImageSet[index] != null) {
//            currentImage = currentImageSet[index];
//            imageSet.setImageBitmap(currentImage);
//        } else {
//            UrlImageViewHelper.setUrlDrawable(imageSet, url,
//                    new UrlImageViewCallback() {
//
//                        @Override
//                        public void onLoaded(ImageView imageView,
//                                             Bitmap loadedBitmap, String url,
//                                             boolean loadedFromCache) {
//                            if (loadedBitmap != null) {
//                                currentImage = loadedBitmap;
//                                currentImageSet[index] = loadedBitmap;
//                            }
//                        }
//                    });
//
//        }
//
//        currentItem = index;
//
//        if (index == 0)
//            previousButton.setEnabled(false);
//        else
//            previousButton.setEnabled(true);
//
//        if (index == sourceContent.getImages().size() - 1)
//            forwardButton.setEnabled(false);
//        else
//            forwardButton.setEnabled(true);
//
//        countTextView.setText((index + 1) + " " + context.getString(R.string.of) + " "
//                + sourceContent.getImages().size());
    }

    private LayoutInflater getLayoutInflater() {
        return (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /**
     * Show or hide the image layout according to the "No Thumbnail" ckeckbox
     */
    private void showHideImage(View image, View parent, boolean show) {
        if (show) {
            image.setVisibility(View.VISIBLE);
            parent.setPadding(5, 5, 5, 5);
            parent.setLayoutParams(new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 2f));
        } else {
            image.setVisibility(View.GONE);
            parent.setPadding(5, 5, 5, 5);
            parent.setLayoutParams(new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 3f));
        }
    }
}
