CREATE SCHEMA IF NOT EXISTS gamification;

-- Seasons table
CREATE TABLE gamification.seasons (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    start_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP WITH TIME ZONE,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Clans table
CREATE TABLE gamification.clans (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) UNIQUE NOT NULL,
    tier VARCHAR(20) NOT NULL DEFAULT 'bronze',
    total_score DECIMAL(10,2) NOT NULL DEFAULT 0,
    leader_id UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Clan members table
CREATE TABLE gamification.clan_members (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    clan_id UUID NOT NULL,
    user_id UUID NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'member',
    joined_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(clan_id, user_id)
);

-- Achievements table
CREATE TABLE gamification.achievements (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    description TEXT,
    milestone INTEGER NOT NULL,
    achievement_type VARCHAR(50) DEFAULT 'reading_count',
    icon_url VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- User achievements table
CREATE TABLE gamification.user_achievements (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    achievement_id UUID NOT NULL,
    unlocked_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    is_visible BOOLEAN NOT NULL DEFAULT false,
    UNIQUE(user_id, achievement_id)
);

-- Daily missions table
CREATE TABLE gamification.daily_missions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    target_type VARCHAR(50) NOT NULL,
    target_count INTEGER NOT NULL,
    xp_reward INTEGER NOT NULL DEFAULT 10,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- User missions table
CREATE TABLE gamification.user_missions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    mission_id UUID NOT NULL,
    progress INTEGER NOT NULL DEFAULT 0,
    claimed BOOLEAN NOT NULL DEFAULT false,
    date DATE NOT NULL,
    UNIQUE(user_id, mission_id, date)
);

-- Buffs table
CREATE TABLE gamification.buffs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    clan_id UUID NOT NULL,
    buff_type VARCHAR(50) NOT NULL,
    multiplier DECIMAL(3,2) NOT NULL,
    activated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    expires_at TIMESTAMP WITH TIME ZONE
);

-- Notifications table
CREATE TABLE gamification.notifications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    notification_type VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT,
    is_read BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Processed events table (idempotency)
CREATE TABLE gamification.processed_events (
    event_id UUID PRIMARY KEY,
    event_type VARCHAR(100) NOT NULL,
    processed_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- User profiles projection table
CREATE TABLE gamification.user_profiles (
    user_id UUID PRIMARY KEY,
    username VARCHAR(100),
    total_xp INTEGER NOT NULL DEFAULT 0,
    level INTEGER NOT NULL DEFAULT 1,
    current_streak INTEGER NOT NULL DEFAULT 0,
    longest_streak INTEGER NOT NULL DEFAULT 0,
    total_readings INTEGER NOT NULL DEFAULT 0,
    total_quizzes INTEGER NOT NULL DEFAULT 0,
    average_accuracy DECIMAL(5,2),
    clan_id UUID,
    clan_name VARCHAR(100),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- User activity stats projection table
CREATE TABLE gamification.user_activity_stats (
    user_id UUID NOT NULL,
    activity_date DATE NOT NULL,
    readings_completed INTEGER NOT NULL DEFAULT 0,
    quizzes_completed INTEGER NOT NULL DEFAULT 0,
    xp_earned INTEGER NOT NULL DEFAULT 0,
    missions_completed INTEGER NOT NULL DEFAULT 0,
    PRIMARY KEY (user_id, activity_date)
);

-- Reading catalog projection table
CREATE TABLE gamification.reading_catalog_projection (
    reading_id UUID PRIMARY KEY,
    total_times_read INTEGER NOT NULL DEFAULT 0,
    total_quizzes_taken INTEGER NOT NULL DEFAULT 0,
    average_accuracy DECIMAL(5,2),
    last_read_at TIMESTAMP WITH TIME ZONE,
    popular_tags TEXT[],
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Indexes
CREATE INDEX idx_clan_members_user ON gamification.clan_members(user_id);
CREATE INDEX idx_clan_members_clan ON gamification.clan_members(clan_id);
CREATE INDEX idx_user_achievements_user ON gamification.user_achievements(user_id);
CREATE INDEX idx_user_missions_user ON gamification.user_missions(user_id);
CREATE INDEX idx_buffs_clan ON gamification.buffs(clan_id);
CREATE INDEX idx_notifications_user ON gamification.notifications(user_id);
CREATE INDEX idx_seasons_active ON gamification.seasons(is_active);
CREATE INDEX idx_processed_events_type ON gamification.processed_events(event_type);
CREATE INDEX idx_user_activity_stats_date ON gamification.user_activity_stats(activity_date);