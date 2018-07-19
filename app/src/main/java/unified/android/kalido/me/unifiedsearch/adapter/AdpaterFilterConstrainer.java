package unified.android.kalido.me.unifiedsearch.adapter;

import androidx.annotation.Nullable;

public interface AdpaterFilterConstrainer<FILTER> {

    @Nullable
    FILTER getFilterConstraint();

    void setFilterConstraint(@Nullable FILTER filterConstraint);
}
