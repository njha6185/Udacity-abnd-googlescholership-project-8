package com.example.bharat.bookbook.activity;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bharat.bookbook.R;
import com.example.bharat.bookbook.data.BookContract.BookEntry;

public class BookCursorAdapter extends CursorAdapter {
    private Context mContext;
    private ProductItemClickListener mListener;

    public BookCursorAdapter(Context context, Cursor c, ProductItemClickListener listener) {
        super(context, c, 0);
        mContext = context;
        mListener = listener;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.library_list_iteam, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView bookTextView = view.findViewById(R.id.name);
        TextView priceTextView = view.findViewById(R.id.price);
        final TextView quantityTextView = view.findViewById(R.id.quantity);

        int bookColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_QUANTITY);

        String bookName = cursor.getString(bookColumnIndex);
        float price = cursor.getFloat(priceColumnIndex);
        int quantity = cursor.getInt(quantityColumnIndex);

        bookTextView.setText(bookName);
        priceTextView.setText(String.valueOf(price));
        quantityTextView.setText(String.valueOf(quantity));

        Button saleButton = view.findViewById(R.id.order_button);
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quantity = quantityTextView.getText().toString();
                if (Integer.valueOf(quantity) > 0) {
                    int newQuantity = Integer.valueOf(quantity) - 1;

                    View relativeLayout = (View) view.getParent();
                    View linearLayout = (View) relativeLayout.getParent();
                    ListView listView = (ListView) linearLayout.getParent();
                    int rows = mListener.onBookOrder(listView.getPositionForView(view), newQuantity);
                    if (rows > 0) {
                        Toast.makeText(mContext, R.string.sold_book, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, R.string.book_cannot_be_sold_unknown_error, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, R.string.not_in_stock, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public interface ProductItemClickListener {
        int onBookOrder(int position, int newQuantity);
    }
}
