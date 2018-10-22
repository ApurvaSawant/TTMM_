package i.ddsweb.ttmm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends ArrayAdapter<AmountInfo>
{

    private Context mContext;
    int mResource;

    public ListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<AmountInfo> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String name = getItem(position).getPersonname();
        String price= getItem(position).getPricee();
        String id =getItem(position).getPersonid();
        String num = getItem(position).getPersonnum();

        AmountInfo amountInfo = new AmountInfo(id,name,num,price);

        LayoutInflater inflater=LayoutInflater.from(mContext);
        convertView= inflater.inflate(mResource,parent,false);

        TextView tvname = convertView.findViewById(R.id.idtextname);
        TextView tvprice =  convertView.findViewById(R.id.idtextprice);

        tvname.setText(name);
        tvprice.setText(price);

        return convertView;

    }
}
