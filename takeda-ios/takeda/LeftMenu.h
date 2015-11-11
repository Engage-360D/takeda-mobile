//
//  LeftMenu.h
//  takeda
//
//  Created by Serg on 3/27/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface LeftMenu : GAITrackedViewController

@property (nonatomic,retain) IBOutlet UITableView *tableView;


-(void)openMainPage;

-(void)openScreenOnIncidentFrom:(id)from;

+(NSArray*)menu_data;

+(int)indexOfItem:(int)itemName inMenu:(NSArray*)menuArray;

@end
