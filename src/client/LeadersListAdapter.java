package client;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

import com.mutiboclient.R;

public class LeadersListAdapter extends BaseAdapter {
    private final ArrayList mData;
    
    protected class MapComparator implements Comparator<Map.Entry<String,Long>> {

		@Override
		public int compare(Entry<String, Long> lhs, Entry<String, Long> rhs) {
			
			Long l1 = lhs.getValue();
			Long l2 = rhs.getValue();
					
			return l2.compareTo(l1);
		}
    }

    public LeadersListAdapter(Map<String, Long> map) {
        mData = new ArrayList();
        mData.addAll(map.entrySet());
        
        Collections.sort(mData, new MapComparator());
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Map.Entry<String, Long> getItem(int position) {
        return (Map.Entry) mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO implement you own logic with ID
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.leader_list_item, parent, false);
        } else {
            result = convertView;
        }

        Map.Entry<String, Long> item = getItem(position);

        // TODO replace findViewById by ViewHolder
        ((TextView) result.findViewById(R.id.FilmName1)).setText(item.getKey());
        ((TextView) result.findViewById(R.id.textView2)).setText(item.getValue().toString());

        return result;
    }
}
