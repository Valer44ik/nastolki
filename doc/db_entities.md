# Main entities

## User
Application user and board game player.
- Nickname
- Avatar
- email / login
- password

## User Statistics
- Games total
- Games won
- Goals achieved
- Rating

## Campaign
- name (could be auto-generated, i.e. User1_vs_User2_230901)
- status_id 
- start date

### Campaign_Status
- name (New, Finished, In Progress)

### Campaign_Players
- campaign_id
- player_id
- mech_id

### Game
- campaing_id
- match_number (sequence number within campaign)
- notes (big text field for any notes after the game)
- end_date (date when status changes to Finished)
- status_id (New, Finished)

#### Game_Attachment
Can attach photos or any other files.
- game_id
- file_name

#### Game_Result
??????

## Pilot
- name
- rank_id 
- status_id 

### Pilot_Rank
- name (*Need a list*)

### Pilot_Status
- name (Ready, Injured, Dead, Captured)


### Pilot2Skil
- pilot_id
- skill_id

### Pilot2Ability
- pilot_id
- ability_id

## Pilot_Skill
- name
- description
- status (Active, Passive)
*Question:* anything else?

## Pilot_Ability
- name
- description


## Mech 
- name
- model_id  
- status_id  
- battle_value (*Question:* is it calculated?)

### Mech_Class
- name
- min_weight
- max_weight

### Mech_Chassis
- name
- description

### Mech_Model
- chassis_id
- class_id (Light, Medium, Heavy, Assault)
- name 
- description

### Mech_Status
- name (Ready, Damaged, Modified, Destroyed, Unavailable)

