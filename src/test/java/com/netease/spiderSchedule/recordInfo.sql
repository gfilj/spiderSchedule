/*选择太老的公众号*/
SELECT * 
from spider_source_info source where source.sourceid not in
(SELECT 
DISTINCT ssi.sourceid
FROM spider_source_info ssi, spider_record_info srit where ssi.sourceid = srit.sourceid and  to_days(now()) - to_days(date_format(srit.update_time,"%Y-%m-%d"))<=9)

