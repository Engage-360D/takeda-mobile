//
//  Files.h
//  takeda
//
//  Created by Alexander Rudenko on 13.02.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#define filePath(fileName) [NSString stringWithFormat:@"%@/%@", [Path JSONFolder],fileName]
#define regionsListFile  @"regionsListFile"
#define resultAnalysesFile [NSString stringWithFormat:@"%@/%@", [Path JResultsFolder],@"analize_results"]
#define userSettingsFile [NSString stringWithFormat:@"%@/%@", [Path JResultsFolder],@"user"]
