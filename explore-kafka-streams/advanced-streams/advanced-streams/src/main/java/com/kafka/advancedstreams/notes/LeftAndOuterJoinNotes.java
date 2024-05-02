package com.kafka.advancedstreams.notes;

public class LeftAndOuterJoinNotes {
    /*
    // left join
    1. If you want to still produce a join record, there is no record present on the right side of
    the join.Then you can use left join.
    2.  the left side of the join received a record and the right side doesn't have the join
    will still be triggered in the case of left join.
    3. Same sql Join
    4. the join result will be matching keys records+left side unmatched key records.
    5. event though there is no matching key present in right side it will trigger join.
     */

    /*
    // outer join
    1. outer join the join will be triggered if there is a record on either side of the join.
    2. when a record is received on either side of the join and there is no matching record on the other side,
     then the combined record will have the null value for the appropriate record.
     3. refer sql outer join
     4. So either side of the join receives a record and if there is no matching record.So in those kind of scenarios,
     the join result will have a null value for the missing value.
     */
}
